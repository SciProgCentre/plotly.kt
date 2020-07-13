package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import scientifik.plotly.doubleInRange
import kotlin.js.JsName


enum class TraceMode {
    lines,

    @JsName("linesMarkers")
    `lines+markers`,
    markers
}

enum class TraceType {
    // Simple
    scatter,
    scattergl,
    bar,
    pie,
    heatmap,
    contour,
    table,

    // Distributions
    box,
    violin,
    histogram,
    histogram2d,
    histogram2dcontour,
//    // Finance
//    ohlc,
//    candlestick,
//    waterfall,
//    // 3D
//    scatter3d,
//    surface,
//    mesh3d,
//    cone,
//    streamtube,
//    volume,
//    isosurface,
//    // Maps
//    scattergeo,
//    choropleth,
//    scattermapbox
//    // Specialized
}

enum class Visible {
    @JsName("true")
    True,

    @JsName("false")
    False,
    legendonly
}

enum class Symbol {
    circle,

    @JsName("triangleUp")
    `triangle-up`,

    @JsName("triangleDown")
    `triangle-down`,

    @JsName("squareCross")
    `square-cross`,

    @JsName("crossThin")
    `cross-thin`,
    cross
}

enum class SizeMode {
    diameter,
    area
}

class MarkerLine : Line() {
    var width by int() // FIXME("number greater than or equal to 0")
    var cauto by boolean(true)
    var cmin by int()
    var cmax by int()
    var cmid by int()

    // var colorscale TODO()
    var autocolorscale by boolean(true)
    var reversescale by boolean()
    // var coloraxis TODO()

    val color = Color(this, "color".asName())

    companion object : SchemeSpec<MarkerLine>(::MarkerLine)
}

enum class TextPosition {
    topLeft,
    topCenter,
    topRight,
    middleLeft,
    middleCenter,
    middleRight,
    bottomLeft,
    bottomCenter,
    bottomRight,
}

class Font : Scheme() {
    var family by string()
    var size by int()

    val color = Color(this, "color".asName())

    companion object : SchemeSpec<Font>(::Font)
}


enum class Direction {
    increasing,
    decreasing
}

enum class CurrentBin {
    include,
    exclude,
    half
}

class Cumulative : Scheme() {
    var enabled by boolean(false)
    var direction by enum(Direction.increasing)
    var currentbin by enum(CurrentBin.include)

    companion object : SchemeSpec<Cumulative>(::Cumulative)
}

enum class HistNorm {
    empty,
    percent,
    probability,
    density,
    probability_density
}

enum class HistFunc {
    count,
    sum,

    @JsName("avg")
    average,
    min,
    max
}


class Bins : Scheme() {
    //FIXME("add categorical coordinate string")
    var start by double()
    var end by double()
    var size by double()

    companion object : SchemeSpec<Bins>(::Bins)
}

class Trace() : Scheme() {
    fun axis(axisName: String) = TraceValues(this, axisName)

    val x = axis(X_AXIS)
    val y = axis(Y_AXIS)
    val z = axis(Z_AXIS)

    var name by string()
    var mode by enum(TraceMode.lines)
    var type by enum(TraceType.scatter)
    var visible by enum(Visible.True)
    var showlegend by boolean(true)
    var legendgroup by string("")
    var opacity by doubleInRange(0.0..1.0, default = 1.0)
    var cumulative by spec(Cumulative)

    // val autobinx by boolean() is not needed
    var histnorm by enum(HistNorm.empty)
    var histfunc by enum(HistFunc.count)
    var xbins by spec(Bins)
    var ybins by spec(Bins)

    //    var line by spec(Line)
    var marker by spec(Marker)
    var text by stringList()
    var textposition by enum(TextPosition.middleCenter)
    var textfont by spec(Font)

    fun textfont(block: Font.() -> Unit) {
        textfont = Font(block)
    }

    fun marker(block: Marker.() -> Unit) {
        marker = Marker(block)
    }

    fun cumulative(block: Cumulative.() -> Unit) {
        cumulative = Cumulative(block)
    }

    fun xbins(block: Bins.() -> Unit) {
        xbins = Bins(block)
    }

    fun ybins(block: Bins.() -> Unit) {
        ybins = Bins(block)
    }

    companion object : SchemeSpec<Trace>(::Trace) {
        const val X_AXIS = "x"
        const val Y_AXIS = "y"
        const val Z_AXIS = "z"
        const val TEXT_AXIS = "text"

        operator fun invoke(xs: Any, ys: Any? = null/*, zs: Any? = null*/, block: Trace.() -> Unit = {}) = invoke {
            block()
            x.set(xs)
            y.set(ys)
        }
    }
}