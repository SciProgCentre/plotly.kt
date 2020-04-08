package scientifik.plotly

import hep.dataforge.meta.*
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
        layout.invoke(block)
    }

    fun addTrace(vararg trace: Trace) {
        data.addAll(trace)
    }

    inline fun trace(xs: DoubleArray, block: Trace.() -> Unit = {}) = trace {
        block()
        x.doubles = xs
    }

    fun trace(xs: DoubleArray, ys: DoubleArray, block: Trace.() -> Unit = {}) {
        addTrace(Trace(xs, ys, block))
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

fun Plot2D.trace(xs: Any, ys: Any? = null, block: Trace.() -> Unit = {}) {
    addTrace(Trace(xs, ys, block))
}

inline fun Plot2D.trace(block: Trace.() -> Unit) {
    addTrace(Trace(block))
}

fun Plot2D.text(block: Annotation.() -> Unit) {
    layout.annotation(block)
}
