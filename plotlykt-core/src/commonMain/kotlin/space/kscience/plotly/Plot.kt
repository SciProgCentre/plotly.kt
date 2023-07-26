package space.kscience.plotly

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.meta.descriptors.Described
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.node
import space.kscience.dataforge.misc.DFBuilder
import space.kscience.plotly.models.Layout
import space.kscience.plotly.models.Trace

/**
 * The main plot class. The changes to plot could be observed by attaching listener to root [meta] property.
 */
@DFBuilder
public class Plot(
    override val meta: ObservableMutableMeta = MutableMeta(),
) : Configurable, MetaRepr, Described {

    /**
     * Ordered list ot traces in the plot
     */
    public val data: List<Trace> by list(Trace)

    /**
     * Layout specification for th plot
     */
    public val layout: Layout by meta.spec(Layout)

    public fun addTrace(trace: Trace) {
        meta.appendAndAttach("data", trace.meta)
    }

    /**
     * Append all traces from [traces] to the plot
     */
    public fun traces(traces: Collection<Trace>) {
        traces.forEach { addTrace(it) }
    }

    /**
     * Append all [traces]
     */
    public fun traces(vararg traces: Trace) {
        traces.forEach { addTrace(it) }
    }

    /**
     * Remove a trace with a given [index] from the plot
     */
    @UnstablePlotlyAPI
    internal fun removeTrace(index: Int) {
        meta.remove("data[$index]")
    }

    override fun toMeta(): Meta = meta

    override val descriptor: MetaDescriptor get() = Companion.descriptor

    public companion object {
        public val descriptor: MetaDescriptor = MetaDescriptor {
            node(Plot::data.name, Trace) {
                multiple = true
            }

            node(Plot::layout.name, Layout)
        }
    }
}

internal fun Plot.toJson(): JsonObject = buildJsonObject {
    put("layout", layout.meta.toJson())
    put("data", buildJsonArray {
        data.forEach { traceData ->
            add(traceData.meta.toJson())
        }
    })
}

/**
 * Convert a plot to Json representation specified by Plotly `newPlot` command.
 */
public fun Plot.toJsonString(): String = toJson().toString()

/**
 * Configure the layout
 */
public inline fun Plot.layout(block: Layout.() -> Unit) {
    layout.invoke(block)
}

/**
 * Add a generic trace
 */
public inline fun Plot.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace(block)
    traces(trace)
    return trace
}