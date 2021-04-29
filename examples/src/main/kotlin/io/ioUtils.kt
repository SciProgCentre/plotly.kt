package io

import krangl.DataFrame
import krangl.readCSV
import space.kscience.plotly.Plotly

fun readResourceAsString(resource: String): String =
    Plotly.javaClass.getResourceAsStream(resource)?.readAllBytes()?.decodeToString()
        ?: error("Resource $resource not found")

fun readResourceAsCsv(resource: String): DataFrame =
    DataFrame.readCSV(Plotly.javaClass.getResource(resource)?.file?.toString() ?: error("Resource $resource not found"))