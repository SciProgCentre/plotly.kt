package scientifik.plotly

import hep.dataforge.meta.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import scientifik.plotly.models.*

@DFBuilder
class Plot : Configurable, MetaRepr {
    override val config: Config = Config()

    val data: List<Trace> by list(Trace)
    val layout: Layout by lazySpec(Layout)

    private fun appendTrace(trace: Trace) {
        config.append("data", trace.config)
    }

    operator fun Trace.unaryPlus() {
        appendTrace(this)
    }

    fun traces(traces: Collection<Trace>) {
        traces.forEach { +it }
    }

    fun traces(vararg traces: Trace) {
        traces.forEach { +it }
    }

    @UnstablePlotlyAPI
    fun removeTrace(index: Int) {
        config.remove("data[$index]")
    }

    @UnstablePlotlyAPI
    fun removeTrace(trace: Trace) {
        val index = data.indexOf(trace)
        removeTrace(index)
    }

    @UnstablePlotlyAPI
    operator fun Trace.unaryMinus() {
        removeTrace(this)
    }

    override fun toMeta(): Meta = config

//    override fun toMeta(): Meta = Meta {
//        "layout" to this@Plot.layout.config
//        "data" to this@Plot.data.map { it.config }
//    }

//    fun toJson(): JsonObject = config.toJson()

    fun toJson(): JsonObject = json {
        "layout" to layout.config.toJson()
        "data" to jsonArray {
            data.forEach {
                +it.config.toJson()
            }
        }
    }
}

inline fun Plot.layout(block: Layout.() -> Unit) {
    layout.invoke(block)
}

inline fun Plot.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace.invoke(block)
    traces(trace)
    return trace
}

inline fun Plot.histogram(block: Histogram.() -> Unit): Histogram {
    val trace = Histogram(block)
    traces(trace)
    return trace
}

inline fun Plot.histogram2d(block: Histogram2D.() -> Unit): Histogram2D {
    val trace = Histogram2D(block)
    traces(trace)
    return trace
}

inline fun Plot.histogram2dcontour(block: Histogram2DContour.() -> Unit): Histogram2DContour {
    val trace = Histogram2DContour(block)
    traces(trace)
    return trace
}

inline fun Plot.pie(block: Pie.() -> Unit): Pie {
    val trace = Pie(block)
    traces(trace)
    return trace
}

inline fun Plot.contour(block: Contour.() -> Unit): Contour {
    val trace = Contour(block)
    traces(trace)
    return trace
}

inline fun Plot.scatter(block: Scatter.() -> Unit): Scatter {
    val trace = Scatter(block)
    traces(trace)
    return trace
}

inline fun Plot.heatmap(block: Heatmap.() -> Unit): Heatmap {
    val trace = Heatmap(block)
    traces(trace)
    return trace
}

inline fun Plot.box(block: Box.() -> Unit): Box {
    val trace = Box(block)
    traces(trace)
    return trace
}

inline fun Plot.violin(block: Violin.() -> Unit): Violin {
    val trace = Violin(block)
    traces(trace)
    return trace
}

inline fun Plot.bar(block: Bar.() -> Unit): Bar {
    val trace = Bar(block)
    traces(trace)
    return trace
}

fun Plot.text(block: Text.() -> Unit) {
    layout.annotation(block)
}

fun Plot.shape(block: Shape.() -> Unit) {
    layout.figure(block)
}
