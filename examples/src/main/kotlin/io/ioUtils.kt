package io

import krangl.DataCol
import krangl.DataFrame
import krangl.readCSV
import space.kscience.dataforge.values.Value
import space.kscience.plotly.Plotly
import space.kscience.plotly.models.TraceValues

fun readResourceAsString(resource: String): String =
    Plotly.javaClass.getResourceAsStream(resource)?.readAllBytes()?.decodeToString()
        ?: error("Resource $resource not found")

fun readResourceAsCsv(resource: String): DataFrame =
    DataFrame.readCSV(Plotly.javaClass.getResource(resource)?.file?.toString() ?: error("Resource $resource not found"))

/**
 * Extension function for using krangl data columns as axis values
 */
operator fun TraceValues.invoke(column: DataCol) {
    set(column.values())
}

fun TraceValues.fromDataFrame(frame: DataFrame, column: String) {
    invoke(frame[column])
}

fun DataCol.dfValues(): List<Value> = values().map { Value.of(it) }