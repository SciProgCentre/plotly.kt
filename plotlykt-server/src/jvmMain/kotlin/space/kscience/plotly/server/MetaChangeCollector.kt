package space.kscience.plotly.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.plotly.Plot


/**
 * A change collector that combines all emitted configuration changes until read, than drops all collected changes
 * and starts new batch.
 */
public class MetaChangeCollector {
    private val mutex = Mutex()
    private var state = MutableMeta()

    public suspend fun collect(name: Name, newItem: Meta?) {
        mutex.withLock {
            state[name] = newItem
        }
    }

    public suspend fun read(): Meta {
        return if (!state.isEmpty()) {
            mutex.withLock {
                state.seal().also {
                    state = MutableMeta()
                }
            }
        } else {
            Meta.EMPTY
        }
    }
}

private fun ObservableMeta.collectChanges(scope: CoroutineScope): MetaChangeCollector = MetaChangeCollector().apply {
    onChange(this) { name ->
        scope.launch {
            collect(name, get(name))
        }
    }
}

private fun ObservableMeta.flowChanges(scope: CoroutineScope, updateInterval: Int): Flow<Meta> {
    val collector = collectChanges(scope)
    return flow {
        while (true) {
            delay(updateInterval.toLong())
            val meta = collector.read()
            if (!meta.isEmpty()) {
                emit(meta)
            }
        }
    }
}

public fun Plot.collectUpdates(
    plotId: String,
    scope: CoroutineScope,
    updateInterval: Int,
): Flow<Update> = meta.flowChanges(scope, updateInterval).transform { change ->
    change["layout"]?.let{ emit(Update.Layout(plotId, it))}
    change.getIndexed("data".asName()).forEach { (index, metaItem) ->
        emit(Update.Trace(plotId, index?.toInt() ?: 0, metaItem))
    }
}