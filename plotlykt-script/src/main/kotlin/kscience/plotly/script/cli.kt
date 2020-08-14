package kscience.plotly.script

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.optional
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import java.io.File
import java.nio.file.Path

fun main(args: Array<String>) {
    val parser = ArgParser("plotlykt-script")
    val input by parser.argument(ArgType.String, description ="Input file path")
    val output by parser.argument(ArgType.String, description = "Output file path").optional()
    val title by parser.option(ArgType.String, fullName = "title", description = "Plot title")
    parser.parse(args)

    require(input.endsWith("plotly.kts"))

    val inputFile: File = File(input)
    val page = Plotly.page(inputFile, title = (title ?: "Plotly.kt"))

    val outputFile: Path? = output?.let{ Path.of(output)}
    page.makeFile(outputFile)
}