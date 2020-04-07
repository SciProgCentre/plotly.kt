package scientifik.plotly

import hep.dataforge.meta.DFBuilder
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaRepr
import hep.dataforge.meta.toJson
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import scientifik.plotly.models.Layout
import scientifik.plotly.models.Trace
import scientifik.plotly.models.layout.Annotation

@DFBuilder
class Plot2D : MetaRepr {
    var data: MutableList<Trace> = ArrayList()
    var layout: Layout = Layout.empty()

    fun layout(block: Layout.() -> Unit) {
        layout = Layout(block)
    }

    fun trace(vararg trace: Trace) {
        data.addAll(trace)
    }

    fun trace(x: DoubleArray, block: Trace.() -> Unit = {}) {
        trace(Trace.build(x, block))
    }

    fun trace(x: DoubleArray, y: DoubleArray, block: Trace.() -> Unit = {}) {
        trace(Trace.build(x, y, block))
    }

    override fun toMeta(): Meta = Meta {
        "layout" to this@Plot2D.layout.config
        "data" to this@Plot2D.data.map { it.config }
    }

    fun toJson(): JsonObject = json {
        "layout" to layout.config.toJson()
        "data" to jsonArray {
            data.forEach {
                +it.config.toJson()
            }
        }
    }

}

fun Plot2D.trace(x: Iterable<Number>, y: Iterable<Number>, block: Trace.() -> Unit = {}) {
    trace(x.map { it.toDouble() }.toDoubleArray(), y.map { it.toDouble() }.toDoubleArray(), block)
}

fun Plot2D.trace(x: Iterable<Number>, block: Trace.() -> Unit = {}) {
    trace(x.map { it.toDouble() }.toDoubleArray(), block)
}

fun Plot2D.trace(block: Trace.() -> Unit) {
    trace(Trace(block))
}

fun Plot2D.text(block: Annotation.() -> Unit) {
    layout.annotation(block)
}
