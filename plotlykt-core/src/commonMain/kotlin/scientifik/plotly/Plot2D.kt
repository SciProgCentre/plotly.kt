package scientifik.plotly

import hep.dataforge.io.toJson
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaRepr
import hep.dataforge.meta.buildMeta
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import scientifik.plotly.models.Layout
import scientifik.plotly.models.Trace

class Plot2D : MetaRepr {
    var data: MutableList<Trace> = ArrayList()
    var layout: Layout = Layout.empty()

    fun layout(block: Layout.() -> Unit) {
        layout = Layout.build(block)
    }

    fun trace(vararg trace: Trace) {
        data.addAll(trace)
    }

    fun trace(x: DoubleArray, y: DoubleArray, block: Trace.() -> Unit = {}) {
        trace(Trace.build(x, y, block))
    }

    override fun toMeta(): Meta = buildMeta {
        "layout" to layout.config
        "data" to data.map { it.config }
    }

    fun toJson(): JsonObject = json {
        "layout" to layout.config.toJson()
        "data" to jsonArray {
            data.forEach {
                +it.config.toJson()
            }
        }
    }

//    companion object : Specification<Plot2D> {
//        override fun wrap(config: Config): Plot2D = Plot2D(config)
//    }
}

