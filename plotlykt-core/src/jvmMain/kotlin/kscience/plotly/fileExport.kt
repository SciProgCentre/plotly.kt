package kscience.plotly

import kotlinx.html.FlowContent
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


internal const val assetsDirectory = "assets"

/**
 * The location of resources for plot.
 */
enum class ResourceLocation {
    /**
     * Use cdn or other remote source for assets
     */
    REMOTE,

    /**
     * Store assets in a sibling folder `plotly-assets` or in a system-wide folder if this is a default temporary file
     */
    LOCAL,

    /**
     * Store assets in a system-window `~/.plotly/plotly-assets` folder
     */
    SYSTEM,

    /**
     * Embed the asset into the html. Could produce very large files.
     */
    EMBED
}

/**
 * Create a standalone html with the plot
 * @param path the reference to html file. If null, create a temporary file
 * @param resourceLocation specifies where to store resources for page display
 * @param show if true, start the browser after file is created
 * @param config represents plotly frame configuration
 */
fun Plot.makeFile(
    path: Path? = null,
    show: Boolean = true,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL,
    config: PlotlyConfig = PlotlyConfig()
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(actualFile, toHTML(inferPlotlyHeader(path, resourceLocation), config = config))
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

/**
 * The same as [Plot.makeFile].
 */
fun PlotlyFragment.makeFile(
    path: Path? = null,
    show: Boolean = true,
    title: String = "Plotly.kt",
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL,
    additionalHeaders: List<HtmlFragment> = emptyList()
) {
    toPage(
        title = title,
        headers = *(additionalHeaders + inferPlotlyHeader(path, resourceLocation)).toTypedArray()
    ).makeFile(path, show)
}


/**
 * Export a page html to a file.
 */
fun PlotlyPage.makeFile(path: Path? = null, show: Boolean = true) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(actualFile, render())
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

fun Plotly.display(
    pageBuilder: FlowContent.(renderer: PlotlyRenderer) -> Unit
) = fragment(pageBuilder).makeFile(null, true)

/**
 * Select a file to save plot to using Swing form.
 */
@UnstablePlotlyAPI
fun selectFile(filter: FileNameExtensionFilter? = null): Path? {
    val fileChooser = JFileChooser()
    fileChooser.dialogTitle = "Specify a file to save"
    if (filter != null) {
        fileChooser.fileFilter = filter
    }

    val userSelection = fileChooser.showSaveDialog(null)

    return if (userSelection == JFileChooser.APPROVE_OPTION) {
        val fileToSave = fileChooser.selectedFile
        fileToSave.toPath()
    } else {
        null
    }
}

/**
 * Output format for Orca export
 */
enum class OrcaFormat {
    png, jpeg, webp, svg, pdf
}

/**
 * Use external [plotly-orca] (https://github.com/plotly/orca) tool to export static image.
 * The tool must be installed externally (for example via conda) and usage patterns could differ for different systems.
 */
@UnstablePlotlyAPI
fun Plot.export(path: Path, format: OrcaFormat = OrcaFormat.svg) {
    val json = toJson().toString()

    val tempFile = Files.createTempFile("plotly-orca-export", ".json")
    Files.writeString(tempFile, json)

    val command = if (System.getProperty("os.name").contains("Windows")) {
        "powershell"
    } else {
        "bash"
    }

    val process = ProcessBuilder(
        "powershell", "orca", "graph", tempFile.toAbsolutePath().toString(),
        "-f", format.toString(),
        "-d", path.parent.toString(),
        "-o", path.fileName.toString(),
        "--verbose"
    ).redirectErrorStream(true).start()
    process.outputStream.close()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    println(output)
    println("Orca finished with code: ${process.waitFor()}")
}