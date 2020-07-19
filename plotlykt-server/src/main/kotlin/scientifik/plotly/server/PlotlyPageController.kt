package scientifik.plotly.server

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import scientifik.plotly.Plot


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
        return mutex.withLock {
            state.seal().also {
                state = Config()
            }
        }
    }
}

/**
 * A structure that collects changes to plot layout as well as trace configuration
 */
private class SinglePlotCollector {
    val layoutCollector = MetaChangeCollector()
    val traceCollectors = HashMap<Int, MetaChangeCollector>()

    fun getTrace(trace: Int) = traceCollectors.getOrPut(trace, ::MetaChangeCollector)
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

    private val channel = Channel<Update>()

    fun updates(): Flow<Update> = channel.receiveAsFlow()

    private val collectors = HashMap<String, SinglePlotCollector>()

    private val mutex = Mutex()

    private suspend fun getCollector(plotId: String) =
        mutex.withLock { collectors.getOrPut(plotId, ::SinglePlotCollector) }


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
                mutex.withLock {
                    collectors.forEach { (plotId, collector) ->
                        val layoutChange = collector.layoutCollector.read()
                        //send layout change if it is not empty
                        if (!layoutChange.isEmpty()) {
                            channel.send(Update.Layout(plotId, layoutChange))
                        }
                        collector.traceCollectors.forEach { (traceNum, traceCollector) ->
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