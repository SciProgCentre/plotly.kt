package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML

interface HtmlHeader {
    operator fun invoke(head: HEAD)
}

object PlotlyCdnHeader : HtmlHeader {
    override fun invoke(head: HEAD) = head.script {
        this.src = "https://cdn.plot.ly/plotly-latest.min.js"
    }
}

fun HEAD.applyHeaders(headers: Array<out HtmlHeader>) {
    //Apply cdn header by default
    if (headers.isEmpty()) {
        PlotlyCdnHeader(this)
    } else {
        headers.forEach {
            it(this)
        }
    }
}

fun FlowContent.staticPlot(
    plot: Plot2D,
    plotId: String = plot.toString(),
    plotlyConfig: PlotlyConfig = PlotlyConfig()
): Plot2D {
    div {
        id = plotId
        script {
            defer = true
            val tracesString = plot.data.toJsonString()
            val layoutString = plot.layout.toJsonString()
            unsafe {
                //language=JavaScript
                +"""
                    
                    Plotly.react(
                        '$plotId',
                        $tracesString,
                        $layoutString,
                        $plotlyConfig
                    );
                    
                """.trimIndent()
            }
        }
    }
    return plot
}

fun FlowContent.staticPlot(
    plotId: String? = null,
    plotlyConfig: PlotlyConfig = PlotlyConfig(),
    builder: Plot2D.() -> Unit
): Plot2D {
    val plot = Plot2D().apply(builder)
    return staticPlot(plot, plotId ?: plot.toString(), plotlyConfig)
}

/**
 * Create a html string from plot
 */
fun Plot2D.toHTML(vararg headers: HtmlHeader, config: PlotlyConfig = PlotlyConfig()): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            applyHeaders(headers)
            title(layout.title ?: "Plotly.kt")
        }
        body {
            staticPlot(this@toHTML, "plot", config)
        }
    }
}

/**
 * Create a custom layout with loaded plotly dependency
 */
fun Plotly.page(
    vararg headers: HtmlHeader,
    title: String? = null,
    bodyBuilder: BODY.() -> Unit
): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            applyHeaders(headers)
            title(title ?: "Plotly.kt")
        }
        body {
            bodyBuilder()
        }
    }
}