package scientifik.plotly.models.layout

import hep.dataforge.meta.scheme.*
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

class Axis : Scheme() {
    var title by string()
    var type by enum(Type.`-`)
    var visible by boolean()
    var autorange by boolean(true)
    var range: Pair<Double, Double>? = null
    var color by string()

    companion object : SchemeSpec<Axis>(::Axis)
}