package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.DoubleArrayValue
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import hep.dataforge.values.doubleArray
import scientifik.plotly.models.general.Line
import kotlin.js.JsName


enum class Mode {
    lines,

    @JsName("linesMarkers")
    `lines+markers`,
    markers
}

enum class Type {
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
    var color by string()
    var cauto by boolean(true)
    var cmin by int()
    var cmax by int()
    var cmid by int()

    // var colorscale TODO()
    var autocolorscale by boolean(true)
    var reversescale by boolean()
    // var coloraxis TODO()

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
    var color by string()

    companion object : SchemeSpec<Font>(::Font)
}


class Marker : Scheme() {
    var symbol: Symbol by enum(Symbol.circle)
    var size by int(6)
    var color by value() // FIXME("Create special type for color)
    var opacity by double() // FIXME("number between or equal to 0 and 1")
    var maxdisplayed by int(0)
    var sizeref by int(1)
    var sizemin by int(0) // FIXME("number greater than or equal to 0")
    var sizemode by enum(SizeMode.diameter)
    var line by spec(MarkerLine)

    fun color(number: Number) {
        color = number.asValue()
    }

    fun color(string: String) {
        color = string.asValue()
    }

    fun color(red: Number, green: Number, blue: Number) {
        color("rgb(${red.toFloat()},${green.toFloat()},${blue.toFloat()})")
    }

    fun color(red: Number, green: Number, blue: Number, alpha: Number) {
        color("rgba(${red.toFloat()},${green.toFloat()},${blue.toFloat()},${alpha.toFloat()})")
    }

    fun colors(colors: Iterable<Any>) {
        color = colors.map { Value.of(it) }.asValue()
    }

    fun line(block: MarkerLine.() -> Unit) {
        line = MarkerLine(block)
    }

    companion object : SchemeSpec<Marker>(::Marker)
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

enum class HisFunc {
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

/**
 * Type-safe accessor class for values in the trace
 */
class TraceValues internal constructor(val trace: Trace, axis: String) {
    var value by trace.value(key = axis.asName())

    var doubles: DoubleArray
        get() = value?.doubleArray ?: doubleArrayOf()
        set(value) {
            this.value = DoubleArrayValue(value)
        }

    var numbers: List<Number>
        get() = value?.list?.map { it.number } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    var strings: List<String>
        get() = value?.list?.map { it.string } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    /**
     * Smart fill for
     */
    fun set(values: Any?) {
        value = when (values) {
            null -> null
            is DoubleArray -> values.asValue()
            is IntArray ->  values.map { it.asValue() }.asValue()
            is Array<*> -> values.map { Value.of(it) }.asValue()
            is Iterable<*> -> values.map { Value.of(it) }.asValue()
            else -> error("Unrecognized values type ${values::class}")
        }
    }

    operator fun invoke(vararg numbers: Number) {
        this.numbers = numbers.asList()
    }

    operator fun invoke(vararg strings: String) {
        this.strings = strings.asList()
    }

}

class Trace() : Scheme() {
    /*
    TODO(Create  specialized classes for scatter, histogram etc )
    trace{
    type = Type.histogram
    ...
    }
    convert to
    histogram{
    ...
    }
     */
    val x = TraceValues(this, "x")
    val y = TraceValues(this, "y")

    var name by string()
    var mode by enum(Mode.lines)
    var type by enum(Type.scatter)
    var visible by enum(Visible.True)
    var showlegend by boolean(true)
    var legendgroup by string("")
    var opacity by double(1.0) // FIXME("number between or equal to 0 and 1")
    var cumulative by spec(Cumulative)

    // val autobinx by boolean() is not needed
    var histnorm by enum(HistNorm.empty)
    var histfunc by enum(HisFunc.count)
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
        operator fun invoke(xs: Any, ys: Any? = null/*, zs: Any? = null*/, block: Trace.() -> Unit = {}) = invoke {
            block()
            x.set(xs)
            y.set(ys)
        }
    }
}