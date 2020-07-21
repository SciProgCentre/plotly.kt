package scientifik.plotly.server

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

private fun Config.collectChanges(scope: CoroutineScope): MetaChangeCollector {
    return MetaChangeCollector().apply {
        onChange(this) { name, _, newItem ->
            scope.launch {
                collect(name, newItem)
            }
        }
    }
}

private fun Config.flowChanges(scope: CoroutineScope, updateInterval: Long): Flow<Meta> {
    val collector = collectChanges(scope)
    return flow {
        while (true) {
            delay(updateInterval)
            val meta = collector.read()
            if (!meta.isEmpty()) {
                emit(meta)
            }
        }
    }
}

fun Plot.collectUpdates(plotId: String, scope: CoroutineScope, updateInterval: Long): Flow<Update> {
    val layoutChangeFlow = layout.config.flowChanges(scope, updateInterval).map { Update.Layout(plotId, it) }
    val traceFlows = data.mapIndexed { index, trace ->
        trace.config.flowChanges(scope, updateInterval).map { Update.Trace(plotId, index, it) }
    }
    return flowOf(layoutChangeFlow, *traceFlows.toTypedArray()).flattenMerge()
}