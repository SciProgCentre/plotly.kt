package kscience.plotly

import hep.dataforge.meta.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kscience.plotly.models.Layout
import kscience.plotly.models.Trace

/**
 * The main plot class. The changes to plot could be observed by attaching listener to root [config] property.
 */
@DFBuilder
public class Plot : Configurable, MetaRepr {
    /**
     *
     */
    override val config: Config = Config()

    /**
     * Ordered list ot traces in the plot
     */
    public val data: List<Trace> by list(Trace)

    /**
     * Layout specification for th plot
     */
    public val layout: Layout by lazySpec(Layout)

    private fun appendTrace(trace: Trace) {
        config.append("data", trace.config)
    }

    /**
     * Append all traces from [traces] to the plot
     */
    public fun traces(traces: Collection<Trace>) {
        traces.forEach { appendTrace(it) }
    }

    /**
     * Append all [traces]
     */
    public fun traces(vararg traces: Trace) {
        traces.forEach { appendTrace(it) }
    }

    /**
     * Remove a trace with a given [index] from the plot
     */
    @UnstablePlotlyAPI
    internal fun removeTrace(index: Int) {
        config.remove("data[$index]")
    }

    override fun toMeta(): Meta = config

    /**
     * Convert a plot to Json representation specified by Plotly `newPlot` command.
     */
    public fun toJson(): JsonObject = buildJsonObject {
        put("layout", layout.config.toJson())
        put("data", buildJsonArray {
            data.forEach {
                add(it.config.toJson())
            }
        })
    }
}

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
    val trace = Trace.invoke(block)
    traces(trace)
    return trace
}