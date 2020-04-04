package scientifik.plotly

import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.title
import kotlinx.html.unsafe
import scientifik.plotly.assets.AssetLocator
import scientifik.plotly.assets.AssetsProvidingMode
import scientifik.plotly.assets.of
import java.awt.Desktop
import java.io.File

/**
 * Create a html string from plot
 *
 * @param provideAssets use this to control the way assets (js-files/images/etc)
 *                      will be referenced. Check [specific entries][AssetsProvidingType]
 *                      for details
 */
fun Plot2D.makeHtml(
    provideAssets: AssetsProvidingMode = AssetsProvidingMode.Online
): String {
    val tracesParsed = data.toJsonString()
    val layoutParsed = layout.toJsonString()

    val a = AssetLocator.of(provideAssets)

    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script { src = a("https://cdn.plot.ly/plotly-latest.min.js") }
                link(
                    rel = "stylesheet",
                    href = a("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
                )
            }
            title(layout.title ?: "Untitled models")
        }
        body {
            div { id = "plot" }
            script {
                unsafe {
                    +"""
                        Plotly.newPlot(
                        'plot',
                        $tracesParsed,
                        $layoutParsed,
                        {showSendToCloud: true});
                    """.trimIndent()
                }
            }
        }
    }
}

/**
 * Create a standalone html with the plot
 * @param file the reference to html file. If null, create a temporary file
 * @param show if true, start the browser after file is created
 * @param provideAssets use this to control the way assets (js-files/images/etc)
 *                      will be referenced. Check [specific entries][AssetsProvidingType]
 *                      for details
 */
fun Plot2D.makeFile(
    file: File? = null,
    show: Boolean = true,
    provideAssets: AssetsProvidingMode = AssetsProvidingMode.Online
) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")

    val html = makeHtml(provideAssets = provideAssets)
    actualFile.writeText(html)

    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}

/**
 * Create a html string for page
 *
 * @param provideAssets use this to control the way assets (js-files/images/etc)
 *                      will be referenced. Check [specific entries][AssetsProvidingType]
 *                      for details
 */
fun PlotGrid.makeHtml(
    provideAssets: AssetsProvidingMode = AssetsProvidingMode.Online
): String {
    val rows = cells.groupBy { it.rowNumber }.mapValues {
        it.value.sortedBy { plot -> plot.colOrderNumber }
    }.toList().sortedBy { it.first }

    val a = AssetLocator.of(provideAssets)

    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script { src = a("https://cdn.plot.ly/plotly-latest.min.js") }
                link(
                    rel = "stylesheet",
                    href = a("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
                )
                script { src = a("https://code.jquery.com/jquery-3.3.1.slim.min.js") }
                script { src = a("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js") }
                script { src = a("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js") }
            }
            title(this@makeHtml.title ?: "Untitled")
        }
        body {
            plotGrid(rows)
            rows.forEach { row ->
                row.second.mapIndexed { idx, cell ->
                    val id = "${row.first}-$idx"
                    val tracesParsed = cell.plot.data.toJsonString()
                    val layoutParsed = cell.plot.layout.toJsonString()
                    script {
                        unsafe {
                            +"""
                                Plotly.newPlot(
                                '$id',
                                $tracesParsed,
                                $layoutParsed,
                                {showSendToCloud: true});
                            """.trimIndent()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Create a standalone html with the page
 * @param file the reference to html file. If null, create a temporary file
 * @param show if true, start the browser after file is created
 * @param provideAssets use this to control the way assets (js-files/images/etc)
 *                      will be referenced. Check [specific entries][AssetsProvidingType]
 *                      for details
 */
fun PlotGrid.makeFile(
    file: File? = null,
    show: Boolean = true,
    provideAssets: AssetsProvidingMode = AssetsProvidingMode.Online
) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")

    val html = makeHtml(provideAssets = provideAssets)
    actualFile.writeText(html)

    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}
