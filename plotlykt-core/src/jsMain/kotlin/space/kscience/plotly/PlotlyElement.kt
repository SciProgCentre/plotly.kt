package space.kscience.plotly

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent
import space.kscience.dataforge.meta.MetaSerializer
import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.firstOrNull
import space.kscience.dataforge.names.startsWith
import space.kscience.plotly.events.PlotlyEvent
import space.kscience.plotly.events.PlotlyEventListenerType
import space.kscience.plotly.events.PlotlyEventPoint

@OptIn(ExperimentalSerializationApi::class)
private fun Scheme.toDynamic(): dynamic = Json.encodeToDynamic(MetaSerializer, meta)

private fun List<Scheme>.toDynamic(): Array<dynamic> = map { it.toDynamic() }.toTypedArray()

/**
 * Attach a plot to this element or update the existing plot
 */
public fun Element.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plot: Plot) {
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

    plot.meta.onChange(this) { name ->
        if (name.startsWith(plot::layout.name.asName())) {
            PlotlyJs.relayout(this@plot, plot.layout.toDynamic())
        } else if (name.firstOrNull()?.body == "data") {
            val traceName = name.firstOrNull()!!
            val traceIndex = traceName.index?.toInt() ?: 0
            val traceData = plot.data[traceIndex].toDynamic()

            Plotly.coordinateNames.forEach { coordinate ->
                val data = traceData[coordinate]
                if (traceData[coordinate] != null) {
                    traceData[coordinate] = arrayOf(data)
                }
            }

            PlotlyJs.restyle(this@plot, traceData, arrayOf(traceIndex))
        }
    }
}

@Deprecated("Change arguments positions", ReplaceWith("plot(plotlyConfig, plot)"))
public fun Element.plot(plot: Plot, plotlyConfig: PlotlyConfig = PlotlyConfig()): Unit = plot(plotlyConfig, plot)

/**
 * Create a plot in this element
 */
public inline fun Element.plot(plotlyConfig: PlotlyConfig = PlotlyConfig(), plotBuilder: Plot.() -> Unit) {
    plot(plotlyConfig, Plot().apply(plotBuilder))
}

public class PlotlyElement(public val div: HTMLElement)

/**
 * Create a div element and render plot in it
 */
public fun TagConsumer<HTMLElement>.plotDiv(
    plotlyConfig: PlotlyConfig = PlotlyConfig(),
    plot: Plot,
): PlotlyElement = PlotlyElement(div("plotly-kt-plot").apply { plot(plotlyConfig, plot) })

/**
 * Render plot in the HTML element using direct plotly API.
 */
public inline fun TagConsumer<HTMLElement>.plotDiv(
    plotlyConfig: PlotlyConfig = PlotlyConfig(),
    plotBuilder: Plot.() -> Unit,
): PlotlyElement = PlotlyElement(div("plotly-kt-plot").apply { plot(plotlyConfig, plotBuilder) })

@OptIn(ExperimentalSerializationApi::class)
public fun PlotlyElement.on(eventType: PlotlyEventListenerType, block: MouseEvent.(PlotlyEvent) -> Unit) {
    div.asDynamic().on(eventType.eventType) { event: PlotMouseEvent ->
        val eventData = PlotlyEvent(event.points.map {
            PlotlyEventPoint(
                curveNumber = it.curveNumber as Int,
                pointNumber = it.pointNumber as? Int,
                x = Value.of(it.x),
                y = Value.of(it.y),
                data = Json.decodeFromDynamic(it.data)
            )
        })
        event.event.block(eventData)
    }
}
