package io


import org.jetbrains.kotlinx.dataframe.AnyCol
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.io.readCSV
import space.kscience.dataforge.values.Value
import space.kscience.plotly.Plotly
import space.kscience.plotly.models.TraceValues

fun readResourceAsString(resource: String): String =
    Plotly.javaClass.getResourceAsStream(resource)?.readAllBytes()?.decodeToString()
        ?: error("Resource $resource not found")

fun readResourceAsCsv(resource: String): AnyFrame =
    DataFrame.readCSV(Plotly.javaClass.getResource(resource)?.file?.toString() ?: error("Resource $resource not found"))

/**
 * Extension function for using krangl data columns as axis values
 */
operator fun TraceValues.invoke(column: AnyCol) {
    set(column.values())
}

fun TraceValues.fromDataFrame(frame: AnyFrame, column: String) {
    invoke(frame[column])
}

fun AnyCol.dfValues(): List<Value> = values().map { Value.of(it) }