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
    //TODO listen to traces changes
    private val _data: MutableList<Trace> = ArrayList()
    val data: List<Trace> get() = _data
    val layout: Layout = Layout.empty()

    fun traces(vararg trace: Trace) {
        _data.addAll(trace)
    }

    fun removeTrace(trace: Trace) {
        _data.remove(trace)
    }

    operator fun Trace.unaryPlus() {
        traces(this)
    }

    operator fun Trace.unaryMinus() {
        removeTrace(this)
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
    traces(trace)
    return trace
}

fun Plot2D.trace(xs: Any, ys: Any? = null, block: Trace.() -> Unit = {}): Trace {
    val trace = Trace(xs, ys, block)
    traces(trace)
    return trace
}

inline fun Plot2D.trace(xs: DoubleArray, block: Trace.() -> Unit = {}) = trace {
    block()
    x.doubles = xs
}

inline fun Plot2D.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace(block)
    traces(trace)
    return trace
}

fun Plot2D.text(block: Annotation.() -> Unit) {
    layout.annotation(block)
}
