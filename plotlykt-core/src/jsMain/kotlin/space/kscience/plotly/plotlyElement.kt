package space.kscience.plotly

import org.w3c.dom.Element
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.firstOrNull
import space.kscience.dataforge.names.startsWith

/**
 * Attach a plot to this element or update existing plot
 */
public fun Element.plot(plot: Plot, plotlyConfig: PlotlyConfig = PlotlyConfig()) {
    val tracesData = plot.config.getIndexed(Plot::data.name).values.map {
        it.node?.toDynamic()
    }.toTypedArray()

    PlotlyJs.react(this, tracesData, plot.layout.rootNode?.toDynamic(), plotlyConfig.rootNode?.toDynamic())

    plot.config.onChange(this) { name, _, _ ->
        if (name.startsWith(plot::layout.name.asName())) {
            PlotlyJs.relayout(this, plot.layout.rootNode?.toDynamic())
        } else if (name.firstOrNull()?.body == "data") {
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

public fun Element.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plotBuilder: Plot.() -> Unit) {
    plot(Plot().apply(plotBuilder), plotlyConfig)
}
