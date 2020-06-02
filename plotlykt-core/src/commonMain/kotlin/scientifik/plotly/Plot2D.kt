package scientifik.plotly

import hep.dataforge.meta.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import scientifik.plotly.models.Annotation
import scientifik.plotly.models.Layout
import scientifik.plotly.models.Trace

@DFBuilder
class Plot2D : MetaRepr {
    var data: MutableList<Trace> = ArrayList()
    var layout: Layout = Layout.empty()

    fun addTrace(vararg trace: Trace) {
        data.addAll(trace)
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

inline fun Plot2D.layout(block: Layout.() -> Unit) {
    layout.invoke(block)
}

fun Plot2D.trace(xs: DoubleArray, ys: DoubleArray, block: Trace.() -> Unit = {}): Trace {
    val trace = Trace(xs, ys, block)
    addTrace(trace)
    return trace
}

fun Plot2D.trace(xs: Any, ys: Any? = null, block: Trace.() -> Unit = {}): Trace {
    val trace = Trace(xs, ys, block)
    addTrace(trace)
    return trace
}

inline fun Plot2D.trace(xs: DoubleArray, block: Trace.() -> Unit = {}) = trace {
    block()
    x.doubles = xs
}

inline fun Plot2D.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace(block)
    addTrace(trace)
    return trace
}

fun Plot2D.text(block: Annotation.() -> Unit) {
    layout.annotation(block)
}
