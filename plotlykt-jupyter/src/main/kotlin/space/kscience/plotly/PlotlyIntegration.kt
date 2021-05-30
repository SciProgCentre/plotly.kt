package space.kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import space.kscience.plotly.Plotly.PLOTLY_CDN

public object PlotlyJupyterConfiguration {
    public var legacyMode: Boolean = false

    /**
     * Switch plotly renderer to the legacy notebook mode (Jupyter classic)
     */
    public fun notebook(): PlotlyHtmlFragment {
        legacyMode = true
        return PlotlyHtmlFragment {
            div {
                style = "color: blue;"
                +"Plotly notebook integration switch into the legacy mode."
            }
        }
    }
}

/**
 * Global plotly jupyter configuration
 */
@UnstablePlotlyAPI
public val Plotly.jupyter: PlotlyJupyterConfiguration
    get() = PlotlyJupyterConfiguration

@UnstablePlotlyAPI
@JupyterLibrary
public class PlotlyIntegration : JupyterIntegration(), PlotlyRenderer {
    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        div {
            id = plotId
            script {
                unsafe {
                    //language=JavaScript
                    +"""
                        if(typeof Plotly !== "undefined"){
                            Plotly.react(
                                    '$plotId',
                                    ${plot.data.toJsonString()},
                                    ${plot.layout.toJsonString()},
                                    ${config.toJsonString()}
                                );       
                        } else {
                            console.error("Plotly not loaded")
                        }
                    """.trimIndent()
                }
            }
        }
        return plot
    }

    private fun renderPlot(plot: Plot): String = createHTML().div {
        plot(plot, config = PlotlyConfig {
            responsive = true
        }, renderer = this@PlotlyIntegration)
    }

    private fun renderFragment(fragment: PlotlyFragment): String = createHTML().div {
        with(fragment) {
            render(this@PlotlyIntegration)
        }
    }

    private fun renderPage(page: PlotlyPage): String = page.copy(renderer = this@PlotlyIntegration).render()

    override fun Builder.onLoaded() {

        resources {
            js("plotly") {
                url(PLOTLY_CDN)
                classPath("js/plotly.min.js")
            }
            js("plotlyConnect") {
                classPath("js/plotlyConnect.js")
            }
        }

        repositories("https://repo.kotlin.link")

        import(
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*"
        )

        import("space.kscience.plotly.jupyter")

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        val renderer = this@PlotlyIntegration

        render<Plot> { plot ->
            if (PlotlyJupyterConfiguration.legacyMode) {
                HTML(
                    Plotly.page(renderer = renderer) {
                        plot(renderer = renderer, plot = plot)
                    }.render(),
                    true
                )
            } else {
                HTML(
                    createHTML().div {
                        renderer.run { renderPlot(plot) }
                    }
                )
            }
        }

        render<PlotlyFragment> { fragment ->
            if (PlotlyJupyterConfiguration.legacyMode) {
                HTML(
                    Plotly.page(renderer = renderer) { renderer ->
                        fragment.render(this, renderer)
                    }.render(), true
                )
            } else {
                HTML(
                    createHTML().div {
                        fragment.render(this, renderer)
                    }
                )
            }
        }

        render<PlotlyPage> {
            HTML(renderPage(it), true)
        }
    }

}
