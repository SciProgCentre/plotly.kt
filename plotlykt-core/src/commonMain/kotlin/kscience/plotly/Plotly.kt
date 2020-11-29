package kscience.plotly

import hep.dataforge.meta.*
import hep.dataforge.names.toName
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlin.js.JsName

/**
 * A namespace for utility functions
 */
@JsName("PlotlyKt")
public object Plotly {
    public const val VERSION: String = "1.54.6"
    public fun plot(block: Plot.() -> Unit): Plot = Plot().apply(block)
}

private fun Scheme.toJson(): JsonObject = config.toJson()

/**
 * Convert any type-safe configurator to json string
 */
public fun Scheme.toJsonString(): String = toJson().toString()


private fun List<Scheme>.toJson(): JsonArray = buildJsonArray {
    forEach { add(it.toJson()) }
}

/**
 * Convert list of type-safe configurators to json array string
 */
public fun List<Scheme>.toJsonString(): String = toJson().toString()

@RequiresOptIn("Unstable API subjected to change in future releases", RequiresOptIn.Level.WARNING)
public annotation class UnstablePlotlyAPI

@RequiresOptIn("This Plotly JS API is not fully supported. Use it at your own risk.", RequiresOptIn.Level.ERROR)
public annotation class UnsupportedPlotlyAPI

public class PlotlyConfig : Scheme() {
    public var editable: Boolean? by boolean()
    public var showEditInChartStudio: Boolean? by boolean()
    public var plotlyServerURL: String? by string()
    public var responsive: Boolean? by boolean()
    public var imageFormat: String? by string("toImageButtonOptions.format".toName())

    public fun withEditorButton() {
        showEditInChartStudio = true
        plotlyServerURL = "https://chart-studio.plotly.com"
    }

    public fun saveAsSvg(){
        imageFormat = "svg"
    }

    override fun toString(): String = toJsonString()

    public companion object : SchemeSpec<PlotlyConfig>(::PlotlyConfig)
}

