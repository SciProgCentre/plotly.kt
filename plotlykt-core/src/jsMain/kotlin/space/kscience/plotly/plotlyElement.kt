package space.kscience.plotly

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToDynamic
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import space.kscience.dataforge.meta.MetaSerializer
import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.rootNode
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.firstOrNull
import space.kscience.dataforge.names.startsWith

@OptIn(ExperimentalSerializationApi::class)
private fun Scheme.toDynamic(): dynamic = rootNode?.let {
    Json.encodeToDynamic(MetaSerializer, it)
} ?: js("{}")

private fun List<Scheme>.toDynamic(): Array<dynamic> = map { it.toDynamic() }.toTypedArray()

/**
 * Attach a plot to this element or update existing plot
 */
public fun Element.plot(plot: Plot, plotlyConfig: PlotlyConfig = PlotlyConfig()) {
    val tracesData = plot.data.toDynamic()
    val layout = plot.layout.toDynamic()

//    console.info("""
//                        Plotly.react(
//                            '$this',
//                            ${JSON.stringify(tracesData)},
//                            ${JSON.stringify(layout)}
//                        );
//                    """.trimIndent())

    PlotlyJs.react(this, tracesData, layout, plotlyConfig.toDynamic())

    plot.config.onChange(this) { name, _, _ ->
        if (name.startsWith(plot::layout.name.asName())) {
            PlotlyJs.relayout(this, plot.layout.toDynamic())
        } else if (name.firstOrNull()?.body == "data") {
            val traceName = name.firstOrNull()!!
            val traceIndex = traceName.index?.toInt() ?: 0
            val traceData = plot.data[traceIndex].toDynamic()

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

public inline fun Element.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plotBuilder: Plot.() -> Unit) {
    plot(Plot().apply(plotBuilder), plotlyConfig)
}

public inline fun TagConsumer<HTMLElement>.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plotBuilder: Plot.() -> Unit) {
    div { }.plot(plotlyConfig, plotBuilder)
}
