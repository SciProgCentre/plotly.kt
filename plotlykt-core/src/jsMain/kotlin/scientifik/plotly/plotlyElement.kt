package scientifik.plotly

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.toDynamic
import org.w3c.dom.HTMLElement

private fun Scheme.toDynamic() = config.toDynamic()

/**
 * Attach a plot to this element or update existing plot
 */
fun HTMLElement.plot(plot: Plot, plotlyConfig: PlotlyConfig = PlotlyConfig()) {
    val traces = plot.data.map { it.toDynamic() }.toTypedArray()
    PlotlyJs.react(this, traces, plot.layout.toDynamic(), plotlyConfig.toDynamic())
    plot.layout.config.onChange(this) { _, _, _ ->
        PlotlyJs.relayout(this, plot.layout.toDynamic())
    }
    plot.data.forEachIndexed { index, trace ->
        trace.config.onChange(this) { _, _, _ ->
            val traceData = trace.toDynamic()

            if (trace.x.value != null) {
                traceData.x = arrayOf(traceData.x)
            }

            if (trace.y.value != null) {
                traceData.y = arrayOf(traceData.y)
            }

            if (trace.z.value != null) {
                traceData.z = arrayOf(traceData.z)
            }

            PlotlyJs.restyle(this, traceData, arrayOf(index))
        }
    }
}

fun HTMLElement.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plotBuilder: Plot.() -> Unit) {
    plot(Plot().apply(plotBuilder), plotlyConfig)
}