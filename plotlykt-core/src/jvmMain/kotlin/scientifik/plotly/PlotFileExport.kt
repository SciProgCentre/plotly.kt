package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.awt.Desktop
import java.io.File

fun Plot2D.makeHtml(): String {
    val tracesParsed = data.toJsonString()
    val layoutParsed = layout.toJsonString()

    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script { src = "https://cdn.plot.ly/plotly-latest.min.js" }
                link(
                    rel = "stylesheet",
                    href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
                )
            }
            title(layout.title ?: "Untitled models")
        }
        body {
            div { id = "plot" }
            script {
                unsafe {
                    +"""
                        var data = [];
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

fun Plot2D.makeFile(file: File? = null, show: Boolean = true) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")
    actualFile.writeText(makeHtml())
    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}


fun PlotGrid.makeHtml(): String {
    val rows = cells.groupBy { it.rowNumber }.mapValues {
        it.value.sortedBy { plot -> plot.colOrderNumber }
    }.toList().sortedBy { it.first }


    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script { src = "https://cdn.plot.ly/plotly-latest.min.js" }
                link(
                    rel = "stylesheet",
                    href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
                )
                script { src = "https://code.jquery.com/jquery-3.3.1.slim.min.js" }
                script { src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" }
                script { src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" }
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
                                var data = [];
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

fun PlotGrid.makeFile(file: File? = null, show: Boolean = true) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")
    actualFile.writeText(makeHtml())
    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}
