package kscience.plotly

import hep.dataforge.meta.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import kscience.plotly.models.Layout
import kscience.plotly.models.Trace

/**
 * The main plot class. The changes to plot could be observed by attaching listener to root [config] property.
 */
@DFBuilder
class Plot : Configurable, MetaRepr {
    /**
     *
     */
    override val config: Config = Config()

    /**
     * Ordered list ot traces in the plot
     */
    val data: List<Trace> by list(Trace)

    /**
     * Layout specification for th plot
     */
    val layout: Layout by lazySpec(Layout)

    private fun appendTrace(trace: Trace) {
        config.append("data", trace.config)
    }

    /**
     * Add a trace to the plot
     */
    operator fun Trace.unaryPlus() {
        appendTrace(this)
    }

    /**
     * Append all traces from [traces] to the plot
     */
    fun traces(traces: Collection<Trace>) {
        traces.forEach { +it }
    }

    /**
     * Append all [traces]
     */
    fun traces(vararg traces: Trace) {
        traces.forEach { +it }
    }

    /**
     * Remove a trace with a given [index] from the plot
     */
    @UnstablePlotlyAPI
    fun removeTrace(index: Int) {
        config.remove("data[$index]")
    }

    /**
     * Remove given [trace] from the plot
     */
    @UnstablePlotlyAPI
    fun removeTrace(trace: Trace) {
        val index = data.indexOf(trace)
        removeTrace(index)
    }

    /**
     * Remove given trace from the plot
     */
    @UnstablePlotlyAPI
    operator fun Trace.unaryMinus() {
        removeTrace(this)
    }

    override fun toMeta(): Meta = config

    /**
     * Convert a plot to Json representation specified by Plotly `newPlot` command.
     */
    fun toJson(): JsonObject = json {
        "layout" to layout.config.toJson()
        "data" to jsonArray {
            data.forEach {
                +it.config.toJson()
            }
        }
    }
}

/**
 * Configure the layout
 */
inline fun Plot.layout(block: Layout.() -> Unit) {
    layout.invoke(block)
}

/**
 * Add a generic trace
 */
inline fun Plot.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace.invoke(block)
    traces(trace)
    return trace
}