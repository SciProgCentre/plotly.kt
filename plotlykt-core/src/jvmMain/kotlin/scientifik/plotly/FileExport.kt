package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.awt.Desktop
import java.io.File

/**
 * Create a html string from plot
 */
fun Plot2D.makeHtml(): String {
    val tracesParsed = data.toJsonString()
    val layoutParsed = layout.toJsonString()

    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script {
                    src = "https://cdn.plot.ly/plotly-latest.min.js"
                }
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
 */
fun Plot2D.makeFile(file: File? = null, show: Boolean = true) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")
    actualFile.mkdirs()
    actualFile.writeText(makeHtml())
    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}

/**
 * Create a html string for page
 */
fun PlotGrid.makeHtml(): String {
    val rows = cells.groupBy { it.rowNumber }.mapValues {
        it.value.sortedBy { plot -> plot.colOrderNumber }
    }.toList().sortedBy { it.first }


    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script { src = "https://cdn.plot.ly/plotly-latest.min.js" }
                script {
                    src = "https://code.jquery.com/jquery-3.5.1.slim.min.js"
                    integrity = "sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
                    attributes["crossorigin"] = "anonymous"
                }
                script {
                    src = "https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
                    integrity = "sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
                    attributes["crossorigin"] = "anonymous"
                }
                script {
                    src = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
                    integrity = "sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
                    attributes["crossorigin"] = "anonymous"
                }
                link {
                    rel = "stylesheet"
                    href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
                    integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
                    attributes["crossorigin"] = "anonymous"
                }
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
 */
fun PlotGrid.makeFile(file: File? = null, show: Boolean = true) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")
    actualFile.mkdirs()
    actualFile.writeText(makeHtml())
    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}

fun PlotGrid.show() = makeFile()