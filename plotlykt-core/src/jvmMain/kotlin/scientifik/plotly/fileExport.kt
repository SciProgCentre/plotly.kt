package scientifik.plotly

import kotlinx.html.FlowContent
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path

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
fun Plot2D.makeFile(
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
 * Make a file with a custom page layout
 */
fun Plotly.makeFile(
    path: Path? = null,
    show: Boolean = true,
    title: String? = null,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL,
    pageBuilder: FlowContent.(container: PlotlyContainer) -> Unit
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(
        actualFile,
        page(inferPlotlyHeader(path, resourceLocation), title = title, pageBuilder = pageBuilder)
    )
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

fun Plotly.show(
    title: String? = null,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL,
    pageBuilder: FlowContent.(container: PlotlyContainer) -> Unit
) = makeFile(null, true, title, resourceLocation, pageBuilder)