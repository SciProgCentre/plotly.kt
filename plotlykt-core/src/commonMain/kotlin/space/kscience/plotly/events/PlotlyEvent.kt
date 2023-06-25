package space.kscience.plotly.events

import kotlinx.serialization.json.JsonElement
import space.kscience.dataforge.meta.Value

public enum class PlotlyEventListenerType(public val eventType: String){
    CLICK("plotly_click"),
    HOVER("plotly_hover"),
    UNHOVER("plotly_unhover"),
    SELECTING("plotly_selecting"),
    SELECTED("plotly_selected")
}

public data class PlotlyEventPoint(
    public val curveNumber: Int,
    public val data: JsonElement,
    public val pointNumber: Int? = null,
    public val x: Value? = null,
    public val y: Value? = null,
//
//    public var data: Trace by spec(Trace)
//    public var fullData: Trace by spec(Trace)
)


public data class PlotlyEvent(public val points: List<PlotlyEventPoint>)


