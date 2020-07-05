package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.ListValue
import hep.dataforge.values.asValue
import hep.dataforge.values.doubleArray
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
    var range: ClosedFloatingPointRange<Double>?
        get() = config["range"]?.value?.doubleArray?.let { it[0]..it[1] }
        set(value) {
            config["range"] = value?.let { ListValue(listOf(value.start.asValue(), value.endInclusive.asValue())) }
        }

    val color = Color(this, "color".asName())

    companion object : SchemeSpec<Axis>(::Axis)
}