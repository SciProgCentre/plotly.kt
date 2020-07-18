package scientifik.plotly

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.unsafe
import java.nio.file.Path


object Jupyter {
    const val JUPYTER_ASSETS_PATH = ".jupyter_kotlin/assets/"

    private val plotlyJupyterHeader = localScriptHeader(
        Path.of("."),
        Path.of(System.getProperty("user.home")).resolve(JUPYTER_ASSETS_PATH)
            .resolve(plotlyResource.removePrefix("/")),
        plotlyResource
    )

    /**
     * Check if plotly loader is started and if not use url loader
     */
    private fun FlowContent.ensurePlotlyLoaded(
        url: String = "https://cdnjs.cloudflare.com/ajax/libs/plotly.js/${PLOTLY_VERSION}/plotly.min.js"
    ) = script {
        unsafe {
            //language=JavaScript
            +"""

                if(typeof window.$PLOTLY_PROMISE_NAME === 'undefined'){
                    console.log("Plotly loader is not defined. Loading from $url.")
                    window.$PLOTLY_PROMISE_NAME = new Promise( (resolve, reject) => {
                        let cdnScript = document.createElement("script");
                        cdnScript.type = "text/javascript";
                        cdnScript.src = "$url";
                        cdnScript.onload = function() {
                            console.log("Successfully loaded Plotly from $url")
                            resolve(Plotly)
                        };
                        cdnScript.onerror = function(event){
                            console.log("Failed to load Plotly from $url")
                            reject("Failed to load Plotly from $url")
                        }
                        document.body.appendChild(cdnScript);
                    });
                }
                    
            """.trimIndent()
        }
    }

    /**
     * Embed plotly script into the jupyter notebook
     */
    fun embedPlotly() = EmbededPlotlyJs

    /**
     * Use local system-wide storage for scripts
     */
    fun localPlotly() = plotlyJupyterHeader

    /**
     * Use cdn script loader for plotly
     */
    fun cdnPlotly() = PlotlyCdnHeader

    fun plotToHTML(plot: Plot): String = createHTML().div {
        ensurePlotlyLoaded()
        plot(plot, config = PlotlyConfig { responsive = true })
    }

    fun pageToHTML(page: PlotlyPage): String = createHTML().div {
        ensurePlotlyLoaded()
        with(page) {
            renderPage(StaticPlotlyContainer)
        }
    }

}