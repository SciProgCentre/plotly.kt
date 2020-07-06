package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.awt.Desktop
import java.io.File


/**
 * Create a html string from plot
 */
fun Plot2D.makeHtml(plotlyPath: String = "https://cdn.plot.ly/plotly-latest.min.js"): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script {
                    src = plotlyPath
                }
            }
            title(layout.title ?: "Untitled models")
        }
        body {
            div { id = "plot" }
            plot("plot", this@makeHtml)
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
    actualFile.writeText(makeHtml("https://cdn.plot.ly/plotly-latest.min.js"))
    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}

/**
 * Create a html string for page
 */
@UnstablePlotlyAPI
fun PlotGrid.makeHtml(plotlyPath: String = "https://cdn.plot.ly/plotly-latest.min.js"): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                script {
                    src = plotlyPath
                }
            }
            title(this@makeHtml.title ?: "Untitled models")
        }
        body {
            plotGrid(this@makeHtml)
        }
    }
}

/**
 * Create a standalone html with the page
 * @param file the reference to html file. If null, create a temporary file
 * @param show if true, start the browser after file is created
 */
@UnstablePlotlyAPI
fun PlotGrid.makeFile(file: File? = null, show: Boolean = true) {
    val actualFile = file ?: File.createTempFile("tempPlot", ".html")
    actualFile.mkdirs()
    actualFile.writeText(makeHtml("https://cdn.plot.ly/plotly-latest.min.js"))
    if (show) {
        Desktop.getDesktop().browse(actualFile.toURI())
    }
}

@UnstablePlotlyAPI
fun PlotGrid.show() = makeFile()