package scientifik.plotly

import hep.dataforge.meta.DFExperimental
import org.w3c.dom.Element
import kotlin.js.json

@DFExperimental
fun Plot2D.show(element: Element, update: Boolean = true) {
    val traces = data.map { it.toJson() }
    PlotlyJs.newPlot(element, traces, layout.toJson(), json("showSendToCloud" to true))
    if(update){
        //TODO do updates
    }
}

@DFExperimental
fun PlotGrid.show(element: Element){

}