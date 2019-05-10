package scientifik.plotly.models.layout

import hep.dataforge.meta.*
import kotlin.js.JsName


enum class Type {
    @JsName("default")
    `-`,
    linear,
    log,
    date,
    category,
    multicategory;
}

class Axis(override val config: Config): Specific {
    var title by string()
    var type by enum(Type.`-`)
    var visible by boolean()
    var autorange by boolean(true)
    var range: Pair<Double, Double>? = null
    var color by string()

    companion object: Specification<Axis>{
        override fun wrap(config: Config): Axis = Axis(config)
    }
}