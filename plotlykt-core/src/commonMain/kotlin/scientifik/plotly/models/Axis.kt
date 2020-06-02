package scientifik.plotly.models

import hep.dataforge.meta.*
import kotlin.js.JsName


enum class AxisType {
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
    var type by enum(AxisType.`-`)
    var visible by boolean()
    var autorange by boolean(true)
    var range: Pair<Double, Double>? = null
    var color by string()

    companion object : SchemeSpec<Axis>(::Axis)
}