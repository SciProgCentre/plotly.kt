package scientifik.plotly

import hep.dataforge.io.toJson
import hep.dataforge.meta.Specific
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlin.js.JsName

/**
 * A namespace for utility functions
 */
@JsName("PlotlyKt")
object Plotly {
    fun plot2D(block: Plot2D.() -> Unit): Plot2D = Plot2D().apply(block)

    fun page(block: PlotGrid.() -> Unit): PlotGrid = PlotGrid().apply(block)
}


fun Specific.toJson(): JsonObject = config.toJson()

/**
 * Convert any type-safe configurator to json string
 */
fun Specific.toJsonString(): String = toJson().toString()


fun List<Specific>.toJson(): JsonArray = jsonArray {
    forEach { +it.toJson() }
}

/**
 * Convert list of type-safe configurators to json array string
 */
fun List<Specific>.toJsonString(): String = toJson().toString()
