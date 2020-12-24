package kscience.plotly

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.names.firstOrNull
import hep.dataforge.names.startsWith
import org.w3c.dom.HTMLElement

/**
 * Attach a plot to this element or update existing plot
 */
public fun HTMLElement.plot(plot: Plot, plotlyConfig: PlotlyConfig = PlotlyConfig()) {
    val tracesData = plot.config.getIndexed(Plot::data.name).values.map {
        it.node?.toDynamic()
    }.toTypedArray()

    PlotlyJs.react(this, tracesData, plot.layout.rootNode?.toDynamic(), plotlyConfig.rootNode?.toDynamic())

    plot.config.onChange(this){ name, _, _ ->
        if(name.startsWith(plot::layout.name.asName())){
            PlotlyJs.relayout(this, plot.layout.rootNode?.toDynamic())
        } else if (name.startsWith(plot::data.name.asName())){
            val traceName = name.firstOrNull()!!
            val traceIndex = traceName.index?.toInt() ?: 0
            val traceData = plot.config[traceName].node?.toDynamic()

            if (traceData.x != null) {
                traceData.x = arrayOf(traceData.x)
            }

            if (traceData.y != null) {
                traceData.y = arrayOf(traceData.y)
            }

            if (traceData.z != null) {
                traceData.z = arrayOf(traceData.z)
            }

            if (traceData.text != null) {
                traceData.text = arrayOf(traceData.text)
            }

            PlotlyJs.restyle(this, traceData, arrayOf(traceIndex))
        }
    }
}

public fun HTMLElement.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plotBuilder: Plot.() -> Unit) {
    plot(Plot().apply(plotBuilder), plotlyConfig)
}
