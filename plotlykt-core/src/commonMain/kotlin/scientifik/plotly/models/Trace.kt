package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.DoubleArrayValue
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import hep.dataforge.values.doubleArray
import scientifik.plotly.doubleGreaterThan
import scientifik.plotly.doubleInRange
import scientifik.plotly.intGreaterThan
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

interface ColorHolder {
    var color: Value?

    fun color(number: Number) {
        color = number.asValue()
    }

    fun color(string: String) {
        val pattern = "^#+([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$".toRegex()
        if (string.startsWith("#")) { // html-like color
            if (string.matches(pattern)) {
                color = string.asValue()
            } else {
                error("$string is not a valid color")
            }
        }
        color = string.asValue()
    }

    fun color(red: Number, green: Number, blue: Number) {
        color("rgb(${red.toFloat()},${green.toFloat()},${blue.toFloat()})")
    }

    fun color(red: Number, green: Number, blue: Number, alpha: Number) {
        color("rgba(${red.toFloat()},${green.toFloat()},${blue.toFloat()},${alpha.toFloat()})")
    }
}

enum class SizeMode {
    diameter,
    area
}

class MarkerLine : Line(), ColorHolder {
    var width by intGreaterThan(0)
    override var color by value()
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

class Font : Scheme(), ColorHolder {
    var family by string()
    var size by intGreaterThan(1)
    override var color by value()

    companion object : SchemeSpec<Font>(::Font)
}

class Marker : Scheme(), ColorHolder {
    var symbol: Symbol by enum(Symbol.circle)
    var size by intGreaterThan(0)
    override  var color by value()
    var opacity by doubleInRange(0.0..1.0)
    var maxdisplayed by intGreaterThan(0)
    var sizeref by int(1)
    var sizemin by intGreaterThan(0)
    var sizemode by enum(SizeMode.diameter)
    var line by spec(MarkerLine)

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
    var size by doubleGreaterThan(0.0)

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

    var numbers: Iterable<Number>
        get() = value?.list?.map { it.number } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    var strings: Iterable<String>
        get() = value?.list?.map { it.string } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    /**
     * Smart fill for trace values. The following types are accepted: [DoubleArray], [IntArray], [Array] of primitive or string,
     * [Iterable] of primitive or string.
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
    fun axis(axisName: String) = TraceValues(this, axisName)

    val x = axis(X_AXIS)
    val y = axis(Y_AXIS)

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
        const val TEXT_AXIS = "text"

        operator fun invoke(xs: Any, ys: Any? = null/*, zs: Any? = null*/, block: Trace.() -> Unit = {}) = invoke {
            block()
            x.set(xs)
            y.set(ys)
        }
    }
}