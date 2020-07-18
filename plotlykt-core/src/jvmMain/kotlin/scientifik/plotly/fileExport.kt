package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
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
    additionalHeaders: List<HtmlFragment> = emptyList(),
    config: PlotlyConfig = PlotlyConfig()
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    val headers = additionalHeaders + inferPlotlyHeader(path, resourceLocation)
    Files.writeString(actualFile, toHTML(*headers.toTypedArray(), config = config))
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

fun PlotlyPage.makeFile(
    path: Path? = null,
    show: Boolean = true,
    title: String? = null,
    additionalHeaders: List<HtmlFragment> = emptyList(),
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    val string = createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            title(title ?: "Plotly.kt")
            (additionalHeaders + inferPlotlyHeader(path, resourceLocation)).forEach { it.visit(consumer) }
        }
        body {
            renderPage(StaticPlotlyContainer)
        }
    }
    Files.writeString(actualFile, string)
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

fun Plotly.show(
    title: String? = null,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL,
    pageBuilder: FlowContent.(container: PlotlyContainer) -> Unit
) = page(pageBuilder).makeFile(null, true, title, resourceLocation = resourceLocation)