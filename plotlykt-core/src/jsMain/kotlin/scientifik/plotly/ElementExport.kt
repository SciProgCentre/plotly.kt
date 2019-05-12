package scientifik.plotly

import hep.dataforge.meta.toDynamic
import org.w3c.dom.Element
import kotlin.js.json

fun Plot2D.show(element: Element, update: Boolean = true) {
    val traces = data.map { it.config.toDynamic() }
    PlotlyJs.newPlot(element, traces, layout.config.toDynamic(), json("showSendToCloud" to true))

}

fun PlotGrid.show(element: Element){

}