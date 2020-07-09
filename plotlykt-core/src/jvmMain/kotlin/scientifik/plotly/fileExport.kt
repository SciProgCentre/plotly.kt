package scientifik.plotly

import kotlinx.html.BODY
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path

enum class ResourceLocation {
    /**
     * Use cdn source for assets
     */
    CDN,

    /**
     * Store assets in a sibling folder `plotly-assets` or in a system-wide folder if this is a default temporary file
     */
    LOCAL,

    /**
     * Store assets in a system-widw `~/.plotly/plotly-assets` folder
     */
    SYSTEM,

    /**
     * Embed the asset into the html
     */
    @Deprecated(
        "Embedded scripts could significantly increase the size of resulting file",
        level = DeprecationLevel.WARNING
    )
    EMBED
}

private fun inferHeader(target: Path?, resourceLocation: ResourceLocation): HtmlHeader = when(resourceLocation){
    ResourceLocation.CDN -> PlotlyCdnHeader
    ResourceLocation.LOCAL -> if(target != null) {
        localPlotlyJs(target)
    } else{
        SystemPlotlyJs
    }
    ResourceLocation.SYSTEM -> SystemPlotlyJs
    ResourceLocation.EMBED -> EmbededPlotlyJs
}

/**
 * Create a standalone html with the plot
 * @param path the reference to html file. If null, create a temporary file
 * @param show if true, start the browser after file is created
 */
fun Plot2D.makeFile(
    path: Path? = null,
    show: Boolean = true,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(actualFile, toHTML(inferHeader(path, resourceLocation)))
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
    bodyBuilder: BODY.() -> Unit
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(actualFile, page(inferHeader(path, resourceLocation), title = title, bodyBuilder = bodyBuilder))
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

fun Plotly.show(
    title: String? = null,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL,
    bodyBuilder: BODY.() -> Unit
) = makeFile(null, true, title, resourceLocation, bodyBuilder)

/**
 * Create a standalone html with the page
 * @param path the reference to html file. If null, create a temporary file
 * @param show if true, start the browser after file is created
 */
@UnstablePlotlyAPI
fun PlotGrid.makeFile(
    path: Path? = null,
    show: Boolean = true,
    resourceLocation: ResourceLocation = ResourceLocation.LOCAL
) {
    val actualFile = path ?: Files.createTempFile("tempPlot", ".html")
    Files.createDirectories(actualFile.parent)
    Files.writeString(actualFile, toHtml(inferHeader(path,resourceLocation)))
    if (show) {
        Desktop.getDesktop().browse(actualFile.toFile().toURI())
    }
}

@UnstablePlotlyAPI
fun PlotGrid.show() = makeFile()