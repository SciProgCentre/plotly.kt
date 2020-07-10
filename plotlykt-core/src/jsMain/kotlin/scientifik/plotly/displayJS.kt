package scientifik.plotly

import org.w3c.dom.Element
import kotlin.js.json

fun Plot2D.show(element: Element, dynamic: Boolean = true) {
    val traces = data.map { it.toJson() }
    PlotlyJs.react(element, traces, layout.toJson(), json("responsive" to true))
    if(dynamic){
        //TODO do updates
    }
}