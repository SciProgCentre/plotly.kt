package scientifik.plotly

import kotlinx.html.FlowContent
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path

internal const val assetsDirectory = "assets"

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
     * Store assets in a system-widw `~/.plotly/plotly-assets` folder
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
 * @param show if true, start the browser after file is created
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

fun PlotlyPage.makeFile(path: Path? = null, show: Boolean = true) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(actualFile, render())
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

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

fun Plotly.display(
    pageBuilder: FlowContent.(container: PlotlyContainer) -> Unit
) = fragment(pageBuilder).makeFile(null, true)