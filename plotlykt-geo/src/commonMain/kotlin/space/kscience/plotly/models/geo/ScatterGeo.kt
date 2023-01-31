package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.plotly.Plot
import space.kscience.plotly.models.TraceType
import space.kscience.plotly.models.TraceValues

public class ScatterGeo : GeoTrace() {
    init {
        type = TraceType.scattergeo
    }

    public val ids: TraceValues by axis
    public val lat: TraceValues by axis
    public val lon: TraceValues by axis

    public companion object : SchemeSpec<ScatterGeo>(::ScatterGeo)
}

public inline fun Plot.scattergeo(block: ScatterGeo.() -> Unit): ScatterGeo {
    val trace = ScatterGeo(block)
    traces(trace)
    return trace
}