package space.kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import space.kscience.plotly.Plotly.PLOTLY_CDN

@UnstablePlotlyAPI
@JupyterLibrary
internal class PlotlyIntegration : JupyterIntegration(), PlotlyRenderer {

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
            js("plotly"){
                url(PLOTLY_CDN)
                classPath("js/plotly.min.js")
            }
            js("plotlyConnect"){
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

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> {
            HTML(renderPlot(it))
        }

        render<PlotlyFragment> {
            HTML(renderFragment(it))
        }

        render<PlotlyPage> {
            HTML(renderPage(it), true)
        }
    }

}
