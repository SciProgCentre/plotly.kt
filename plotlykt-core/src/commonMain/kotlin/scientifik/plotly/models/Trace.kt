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
    scatter
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

class Marker(override val config: Config) : Specific {
    var symbol by enum(Symbol.circle)
    var size by int(6)

    companion object : Specification<Marker> {
        override fun wrap(config: Config): Marker = Marker(config)
    }
}

class Trace(override val config: Config) : Specific {
    var x by numberList()
    var y by numberList()

    var name by string()
    var mode by enum(Mode.lines)
    var type by enum(Type.scatter)
    var line by spec(Line)
    var marker by spec(Marker)
    var text by stringList()

    companion object : Specification<Trace> {
        fun build(x: DoubleArray, y: DoubleArray, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x.asList()
            this.y = y.asList()
        }

        fun build(x: List<Double>, y: List<Double>, block: Trace.() -> Unit = {}): Trace = build(block).apply {
            this.x = x
            this.y = y
        }

        override fun wrap(config: Config): Trace = Trace(config)
    }
}