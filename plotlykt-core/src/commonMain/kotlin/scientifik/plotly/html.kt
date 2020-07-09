package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML

interface HtmlHeader {
    operator fun invoke(meta: META)
}

object PlotlyCdnHeader : HtmlHeader {
    override fun invoke(meta: META) = meta.script {
        this.src = "https://cdn.plot.ly/plotly-latest.min.js"
    }
}

fun META.applyHeaders(headers: Array<out HtmlHeader>){
    //Apply cdn header by default
    if(headers.isEmpty()){
        PlotlyCdnHeader(this)
    } else {
        headers.forEach {
            it(this)
        }
    }
}

fun FlowContent.plot(plot: Plot2D, plotId: String = plot.toString()): Plot2D {
    div {
        id = plotId
        script {
            val tracesString = plot.data.toJsonString()
            val layoutString = plot.layout.toJsonString()
            unsafe {
                //language=JavaScript
                +"""
                    Plotly.newPlot(
                        '$plotId',
                         $tracesString,
                         $layoutString,
                         {showSendToCloud: true}
                    );
                """.trimIndent()
            }
        }
    }
    return plot
}

fun FlowContent.plot(plotId: String? = null, builder: Plot2D.() -> Unit): Plot2D {
    val plot = Plot2D().apply(builder)
    return plot(plot, plotId ?: plot.toString())
}

/**
 * Create a html string from plot
 */
fun Plot2D.toHTML(vararg headers: HtmlHeader): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
                applyHeaders(headers)
            }
            title(layout.title ?: "Plotly.kt")
        }
        body {
            plot(this@toHTML, "plot")
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
                applyHeaders(headers)
            }
            title(title ?: "Plotly.kt")
        }
        body {
            bodyBuilder()
        }
    }
}