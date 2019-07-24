package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.models.layout.Line
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
    histogram2dcontour
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

class Line(override val config: Config) : Specific {
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

    companion object : Specification<Line> {
        override fun wrap(config: Config): Line = Line(config)
    }
}

enum class TextPosition {
    @JsName("top left")
    topLeft,
    @JsName("top center")
    topCenter,
    @JsName("top right")
    topRight,
    @JsName("middle left")
    middleLeft,
    @JsName("middle center")
    middleCenter,
    @JsName("middle right")
    middleRight,
    @JsName("bottom left")
    bottomLeft,
    @JsName("bottom center")
    bottomCenter,
    @JsName("bottom right")
    bottomRight,
}

class Font(override val config: Config) : Specific {
    var family by string()
    var size by int()
    var color by string()

    companion object : Specification<Font> {
        override fun wrap(config: Config): Font = Font(config)
    }
}


class Marker(override val config: Config) : Specific {
    var symbol by enum(Symbol.circle)
    var size by int(6)
    var color by numberList() // FIXME("Create special type for color)
    var opacity by double() // FIXME("number between or equal to 0 and 1")
    var maxdisplayed by int(0)
    var sizeref by int(1)
    var sizemin by int(0) // FIXME("number greater than or equal to 0")
    var sizemode by enum(SizeMode.diameter)
    var line by spec(Line)

    fun line(block: Line.() -> Unit) {
        line = Line.build(block)
    }

    companion object : Specification<Marker> {
        override fun wrap(config: Config): Marker = Marker(config)
    }
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

class Cumulative(override val config: Config) : Specific {
    var enabled by boolean(false)
    var direction by enum(Direction.increasing)
    var currentbin by enum(CurrentBin.include)

    companion object : Specification<Cumulative> {
        override fun wrap(config: Config): Cumulative = Cumulative(config)
    }
}

enum class HistNorm {
    @JsName("")
    empty,
    percent,
    probability,
    density,
    @JsName("probability density")
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


class Bins(override val config: Config) : Specific {
    //FIXME("add categorical coordinate string")
    var start by double()
    var end by double()
    var size by double()

    companion object : Specification<Bins> {
        override fun wrap(config: Config): Bins = Bins(config)
    }
}

class Trace(override val config: Config) : Specific {
    var x by numberList()
    var y by numberList()

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
    var line by spec(Line)
    var marker by spec(Marker)
    var text by stringList()
    var textposition by enum(TextPosition.middleCenter)
    var textfont by spec(Font)

    fun textfont(block: Font.() -> Unit) {
        textfont = Font.build(block)
    }

    fun marker(block: Marker.() -> Unit) {
        marker = Marker.build(block)
    }

    fun cumulative(block: Cumulative.() -> Unit) {
        cumulative = Cumulative.build(block)
    }

    fun xbins(block: Bins.() -> Unit) {
        xbins = Bins.build(block)
    }

    fun ybins(block: Bins.() -> Unit) {
        ybins = Bins.build(block)
    }

    companion object : Specification<Trace> {

        fun build(x: DoubleArray, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x.asList()
        }

        fun build(x: List<Double>, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x
        }

//        fun build(x: Iterable<Number>,  block: Trace.() -> Unit = {}): Trace = build(block).apply {
//            this.x = x.map { it.toDouble() }
//        }

        fun build(x: DoubleArray, y: DoubleArray, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x.asList()
            this.y = y.asList()
        }

        fun build(x: List<Double>, y: List<Double>, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x
            this.y = y
        }

        fun build(x: Iterable<Number>, y: Iterable<Number>, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x.map { it.toDouble() }
            this.y = y.map { it.toDouble() }
        }

        fun build(points: Iterable<Pair<Double, Double>>, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            val x = ArrayList<Double>()
            val y = ArrayList<Double>()
            points.forEach {
                x.add(it.first)
                y.add(it.second)
            }
            this.x = x
            this.y = y
        }

        override fun wrap(config: Config): Trace = Trace(config)
    }
}