package io

import krangl.DataFrame
import krangl.readCSV
import scientifik.plotly.Plotly

@OptIn(ExperimentalStdlibApi::class)
fun readResourceAsString(resource: String): String =
        Plotly.javaClass.getResourceAsStream(resource).readAllBytes().decodeToString()

fun readResourceAsCsv(resource: String): DataFrame =
        DataFrame.readCSV(Plotly.javaClass.getResource(resource).file.toString())