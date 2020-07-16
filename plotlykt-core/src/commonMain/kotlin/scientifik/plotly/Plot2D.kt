package scientifik.plotly

import hep.dataforge.meta.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import scientifik.plotly.models.Annotation
import scientifik.plotly.models.*

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
    val trace = Trace.invoke(xs, ys, block)
    traces(trace)
    return trace
}

fun Plot2D.trace(xs: Any, ys: Any? = null, block: Trace.() -> Unit = {}): Trace {
    val trace = Trace.invoke(xs, ys, block)
    traces(trace)
    return trace
}

inline fun Plot2D.trace(xs: DoubleArray, block: Trace.() -> Unit = {}) = trace {
    block()
    x.doubles = xs
}

inline fun Plot2D.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace.invoke(block)
    traces(trace)
    return trace
}


inline fun Plot2D.histogram(block: Histogram.() -> Unit): Histogram {
    val trace = Histogram(block)
    trace.type  = TraceType.histogram
    traces(trace)
    return trace
}

inline fun Plot2D.histogram(xs: DoubleArray, block: Histogram.() -> Unit = {}) = histogram {
    block()
    type = TraceType.histogram
    x.doubles = xs
}

inline fun Plot2D.histogram2d(block: Histogram2D.() -> Unit): Histogram2D {
    val trace = Histogram2D(block)
    trace.type  = TraceType.histogram2d
    traces(trace)
    return trace
}

inline fun Plot2D.histogram2d(xs: DoubleArray, block: Trace.() -> Unit = {}) = histogram2d {
    block()
    type = TraceType.histogram2d
    x.doubles = xs
}

inline fun Plot2D.histogram2dcontour(block: Histogram2DContour.() -> Unit): Histogram2DContour {
    val trace = Histogram2DContour(block)
    trace.type  = TraceType.histogram2dcontour
    traces(trace)
    return trace
}

inline fun Plot2D.histogram2dcontour(xs: DoubleArray, block: Histogram2DContour.() -> Unit = {}) = histogram2dcontour {
    block()
    type = TraceType.histogram2dcontour
    x.doubles = xs
}

inline fun Plot2D.pie(block: Pie.() -> Unit): Pie {
    val trace = Pie(block)
    trace.type  = TraceType.pie
    traces(trace)
    return trace
}

inline fun Plot2D.pie(xs: DoubleArray, block: Trace.() -> Unit = {}) = pie {
    block()
    type = TraceType.pie
    x.doubles = xs
}

inline fun Plot2D.contour(block: Contour.() -> Unit): Contour {
    val trace = Contour(block)
    trace.type  = TraceType.contour
    traces(trace)
    return trace
}

inline fun Plot2D.contour(xs: DoubleArray, block: Trace.() -> Unit = {}) = contour {
    block()
    type = TraceType.contour
    x.doubles = xs
}

inline fun Plot2D.scatter(block: Scatter.() -> Unit): Scatter {
    val trace = Scatter(block)
    trace.type  = TraceType.scatter
    traces(trace)
    return trace
}

inline fun Plot2D.scatter(xs: DoubleArray, block: Trace.() -> Unit = {}) = scatter {
    block()
    type = TraceType.scatter
    x.doubles = xs
}

inline fun Plot2D.heatmap(block: Heatmap.() -> Unit): Heatmap {
    val trace = Heatmap(block)
    trace.type  = TraceType.heatmap
    traces(trace)
    return trace
}

inline fun Plot2D.heatmap(xs: DoubleArray, block: Trace.() -> Unit = {}) = heatmap {
    block()
    type = TraceType.heatmap
    x.doubles = xs
}

inline fun Plot2D.box(block: Box.() -> Unit): Box {
    val trace = Box(block)
    trace.type  = TraceType.box
    traces(trace)
    return trace
}

inline fun Plot2D.box(xs: DoubleArray, block: Trace.() -> Unit = {}) = box {
    block()
    type = TraceType.box
    x.doubles = xs
}

inline fun Plot2D.violin(block: Violin.() -> Unit): Violin {
    val trace = Violin(block)
    trace.type  = TraceType.violin
    traces(trace)
    return trace
}

inline fun Plot2D.violin(xs: DoubleArray, block: Trace.() -> Unit = {}) = violin {
    block()
    type = TraceType.violin
    x.doubles = xs
}

inline fun Plot2D.bar(block: Bar.() -> Unit): Bar {
    val trace = Bar(block)
    trace.type  = TraceType.bar
    traces(trace)
    return trace
}

inline fun Plot2D.bar(xs: DoubleArray, block: Trace.() -> Unit = {}) = bar {
    block()
    type = TraceType.bar
    x.doubles = xs
}

fun Plot2D.text(block: Annotation.() -> Unit) {
    layout.annotation(block)
}
