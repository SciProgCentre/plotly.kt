package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import scientifik.plotly.list
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

class Trace : Scheme() {
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
    var x: List<Value> by list()
    var y: List<Value> by list()

    fun x(vararg xs: Number) {
        x = xs.map { it.asValue() }
    }

    fun y(vararg ys: Number) {
        y = ys.map { it.asValue() }
    }

    fun x(numbers: Iterable<Number>){
        x = numbers.map { it.asValue() }
    }

    fun y(numbers: Iterable<Number>){
        y = numbers.map { it.asValue() }
    }

    fun categoryX(vararg xs: String) {
        x = xs.map { it.asValue() }
    }

    fun categoryY(vararg ys: String) {
        y = ys.map { it.asValue() }
    }

    fun categoryX(numbers: Iterable<String>){
        x = numbers.map { it.asValue() }
    }

    fun categoryY(numbers: Iterable<String>){
        y = numbers.map { it.asValue() }
    }

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

        fun build(x: DoubleArray, block: Trace.() -> Unit = {}): Trace = invoke(block).apply {
            this.x = x.map { it.asValue() }
        }

        fun build(x: List<Double>, block: Trace.() -> Unit = {}): Trace = invoke(block).apply {
            this.x = x.map { it.asValue() }
        }

        // FIXME("This code don't compile")
//        fun build(x: Iterable<Number>,  block: Trace.() -> Unit = {}): Trace = build(block).apply {
//            this.x = x.map { it.toDouble() }
//        }

        fun build(x: DoubleArray, y: DoubleArray, block: Trace.() -> Unit = {}): Trace = invoke(block).apply {
            this.x = x.map { it.asValue() }
            this.y = y.map { it.asValue() }
        }

        fun build(x: List<Double>, y: List<Double>, block: Trace.() -> Unit = {}): Trace = invoke(block).apply {
            this.x = x.map { it.asValue() }
            this.y = y.map { it.asValue() }
        }

        fun build(x: Iterable<Number>, y: Iterable<Number>, block: Trace.() -> Unit = {}): Trace = invoke(block).apply {
            this.x = x.map { it.asValue() }
            this.y = y.map { it.asValue() }
        }

        fun build(points: Iterable<Pair<Double, Double>>, block: Trace.() -> Unit = {}): Trace = invoke(block).apply {
            val x = ArrayList<Value>()
            val y = ArrayList<Value>()
            points.forEach {
                x.add(it.first.asValue())
                y.add(it.second.asValue())
            }
            this.x = x
            this.y = y
        }
    }
}