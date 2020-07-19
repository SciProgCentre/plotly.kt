package scientifik.plotly.server

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import scientifik.plotly.Plot
import java.util.concurrent.ConcurrentHashMap


/**
 * A change collector that combines all emitted configuration changes until read, than drops all collected changes
 * and starts new batch.
 */
class MetaChangeCollector {
    private val mutex = Mutex()
    private var state = Config()

    suspend fun collect(name: Name, newItem: MetaItem<*>?) {
        mutex.withLock {
            state[name] = newItem
        }
    }

    suspend fun read(): Meta {
        return if (!state.isEmpty()) {
            mutex.withLock {
                state.seal().also {
                    state = Config()
                }
            }
        } else {
            Meta.EMPTY
        }
    }
}

/**
 * A structure that collects changes to plot layout as well as trace configuration
 */
private class SinglePlotCollector {
    val layoutCollector = MetaChangeCollector()
    val traceCollectors = ConcurrentHashMap<Int, MetaChangeCollector>()

    fun getTrace(trace: Int): MetaChangeCollector = traceCollectors.getOrPut(trace, ::MetaChangeCollector)
}


class PlotlyPageController(
    val scope: CoroutineScope,
    val updateInterval: Long
) {
    /**
     * A collection of all plots served on this page
     */
    val _plots = HashMap<String, Plot>()
    val plots: Map<String, Plot> get() = _plots

    private val channel = BroadcastChannel<Update>(100)

    fun subscribe(): ReceiveChannel<Update> = channel.openSubscription()

    private val collectors = ConcurrentHashMap<String, SinglePlotCollector>()

    private fun getCollector(plotId: String) = collectors.getOrPut(plotId, ::SinglePlotCollector)

    private fun traceChanged(plotId: String, traceId: Int, itemName: Name, item: MetaItem<*>?) {
        scope.launch {
            getCollector(plotId).getTrace(traceId).collect(itemName, item)
        }
    }

    private fun layoutChanged(plotId: String, itemName: Name, item: MetaItem<*>?) {
        scope.launch {
            getCollector(plotId).layoutCollector.collect(itemName, item)
        }
    }

    fun listenTo(plot: Plot, plotId: String) {
        _plots[plotId] = plot
        plot.data.forEachIndexed { index, trace ->
            trace.config.onChange(this) { name, _, newItem ->
                traceChanged(plotId, index, name, newItem)
            }
        }
        plot.layout.config.onChange(this) { name, _, newItem ->
            layoutChanged(plotId, name, newItem)
        }
    }

    /**
     * Start sending updates in the scope it was created
     */

    init {
        scope.launch {
            while (isActive) {
                delay(updateInterval)
                //checking all incoming changes
                collectors.forEach { (plotId, collector) ->
                    launch {
                        val layoutChange = collector.layoutCollector.read()
                        //send layout change if it is not empty
                        if (!layoutChange.isEmpty()) {
                            channel.send(Update.Layout(plotId, layoutChange))
                        }
                    }
                    collector.traceCollectors.forEach { (traceNum, traceCollector) ->
                        launch {
                            val change = traceCollector.read()
                            //send trace change if it is not empty
                            if (!change.isEmpty()) {
                                channel.send(Update.Trace(plotId, traceNum, change))
                            }
                        }
                    }

                }
            }
        }
    }

}