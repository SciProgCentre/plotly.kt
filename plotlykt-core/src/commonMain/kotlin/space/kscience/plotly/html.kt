package space.kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML


public class PlotlyHtmlFragment(public val visit: TagConsumer<*>.() -> Unit) {
    override fun toString(): String {
        return createHTML().also(visit).finalize()
    }
}

public operator fun PlotlyHtmlFragment.plus(other: PlotlyHtmlFragment): PlotlyHtmlFragment = PlotlyHtmlFragment {
    this@plus.run { visit() }
    other.run { visit() }
}

public val cdnPlotlyHeader: PlotlyHtmlFragment = PlotlyHtmlFragment {
    script {
        type = "text/javascript"
        src = Plotly.PLOTLY_CDN
    }
}

/**
 * Create a html (including headers) string from plot
 */
public fun Plot.toHTML(
    vararg headers: PlotlyHtmlFragment = arrayOf(cdnPlotlyHeader),
    config: PlotlyConfig = PlotlyConfig(),
): String = createHTML().html {
    head {
        meta {
            charset = "utf-8"
        }
        title(layout.title ?: "Plotly.kt")
        headers.forEach {
            it.visit(consumer)
        }
    }
    body {
        StaticPlotlyRenderer.run {
            renderPlot(this@toHTML, this@toHTML.toString(), config)
        }
    }
}

public val mathJaxHeader: PlotlyHtmlFragment = PlotlyHtmlFragment {
    script {
        type = "text/x-mathjax-config"
        unsafe {
            +"""
            MathJax.Hub.Config({
                tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
            });
            """
        }
    }
    script {
        type = "text/javascript"
        async = true
        src = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-MML-AM_SVG"
    }
}