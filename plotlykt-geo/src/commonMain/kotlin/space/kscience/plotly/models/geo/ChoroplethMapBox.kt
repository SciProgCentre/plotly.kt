package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.plotly.Plot
import space.kscience.plotly.models.TraceType

public class ChoroplethMapBox : GeoTrace() {
    init {
        type = TraceType.choroplethmapbox
    }

    public companion object : SchemeSpec<ChoroplethMapBox>(::ChoroplethMapBox)
}

public inline fun Plot.choroplethMapBox(block: ChoroplethMapBox.() -> Unit): ChoroplethMapBox {
    val trace = ChoroplethMapBox(block)
    traces(trace)
    return trace
}