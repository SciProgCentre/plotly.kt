package scientifik.plotly

/**
 * Save image in svg format using orca from python script.
 * @param json plot converted to json string
 * @param file name, e.g. "fig.svg"
 * @param directory folder to save image to
 */
@UnstablePlotlyAPI
fun imageExport(json: String, file: String, directory: String) {
    val scriptName = "./examples/src/main/resources/svgOrcaScript.py"
    val format = file.substring(file.indexOf('.') + 1)
    if (format !in listOf("png", "jpeg", "webp", "svg", "pdf", "eps", "emf")) {
        throw Error("Not supported format: $format")
    }
    val processBuilder = ProcessBuilder(scriptName, json, file, directory, format)
    val process = processBuilder.inheritIO().start()
    println(process.waitFor())
}
