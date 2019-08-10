package scientifik.plotly.server

import hep.dataforge.meta.MetaItem
import hep.dataforge.meta.isEmpty
import hep.dataforge.names.Name
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import scientifik.plotly.PageListener


/**
 * A structure that collects changes to plot layout as well as trace configuration
 */
private class PlotCollector {
    val layoutCollector = MetaChangeCollector()
    val traceCollectors = HashMap<Int, MetaChangeCollector>()

    fun getTrace(trace: Int) = traceCollectors.getOrPut(trace, ::MetaChangeCollector)
}

/**
 *
 */
class CollectingPageListener(
    scope: CoroutineScope,
    val updateChannel: SendChannel<Update>,
    val pageId: String,
    val interval: Long
) : PageListener {

    private val collectors = HashMap<String, PlotCollector>()

    private val scope = scope + Job()

    private var listenerJob: Job? = null

    private fun getCollector(plotId: String) = collectors.getOrPut(plotId, ::PlotCollector)

    override fun traceChanged(plotId: String, traceId: Int, itemName: Name, item: MetaItem<*>?) {
        start()
        scope.launch {
            getCollector(plotId).getTrace(traceId).collect(itemName, item)
        }
    }

    override fun layoutChanged(plotId: String, itemName: Name, item: MetaItem<*>?) {
        start()
        scope.launch {
            getCollector(plotId).layoutCollector.collect(itemName, item)
        }
    }

    /**
     * Start sending updates in the scope it was created
     */
    fun start() {
        if (listenerJob?.isActive != true) {
             listenerJob = scope.launch {
                while (isActive) {
                    delay(interval)
                    //checking all
                    collectors.forEach { (plotId, collector) ->
                        val layoutChange = collector.layoutCollector.read()
                        //send layout change if it is not empty
                        if (!layoutChange.isEmpty()) {
                            updateChannel.send(Update.Layout(pageId, plotId, layoutChange))
                        }
                        collector.traceCollectors.forEach { (traceNum, traceCollector) ->
                            val change = traceCollector.read()
                            //send trace change if it is not empty
                            if (!change.isEmpty()) {
                                updateChannel.send(Update.Trace(pageId, plotId, traceNum, change))
                            }
                        }
                    }
                }
            }
        }
    }

    fun stop() {
        listenerJob?.cancel()
    }

}