package space.kscience.plotly.events

import space.kscience.dataforge.meta.*
import space.kscience.plotly.list
import space.kscience.plotly.models.Trace

public enum class PlotlyEventListenerType(public val typeName: String){
    CLICK("plotly_click"),
    HOVER("plotly_hover"),
    UNHOVER("plotly_unhover"),
    SELECTING("plotly_selecting"),
    SELECTED("plotly_selected")
}

public class PlotlyEventPoint : Scheme() {
    public val curveNumber: Int by int(1)
    public val pointNumber: Int? by int()
    public val x: Value? by value()
    public val y: Value? by value()

    public val data: Trace by spec(Trace)
    public val fullData: Trace by spec(Trace)

    public companion object : SchemeSpec<PlotlyEventPoint>(::PlotlyEventPoint)
}


public class PlotlyEvent : Scheme() {
    public val points: List<PlotlyEventPoint> by list(PlotlyEventPoint)

    public companion object : SchemeSpec<PlotlyEvent>(::PlotlyEvent)
}


