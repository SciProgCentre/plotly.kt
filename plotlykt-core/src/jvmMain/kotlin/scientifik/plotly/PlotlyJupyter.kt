package scientifik.plotly

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.unsafe
import java.nio.file.Path

object PlotlyJupyter {
    const val JUPYTER_ASSETS_PATH = ".jupyter_kotlin/assets/"

    private val plotlyJupyterHeader = LocalScriptHeader(
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

//    fun initScript(): String {
//        val filePath = checkOrStoreFile(
//            Path.of("."),
//            Path.of(System.getProperty("user.home")).resolve(JUPYTER_ASSETS_PATH + plotlyResource),
//            plotlyResource
//        ).toAbsolutePath().toString().replace("\\", "/")
//        return createHTML().div {
//            val divID = "${PLOTLY_LOADER}-init"
//            id = divID
//            script {
//                unsafe {
//                    //language=JavaScript
//                    +"""
//
//                    if(typeof window.$PLOTLY_LOADER === 'undefined'){
//                        let element = document.getElementById("$divID");
//                        window.$PLOTLY_LOADER = new Promise((resolve, reject) => {
//                            let script = document.createElement("script");
//                            script.type = "text/javascript";
//                            script.src = "${filePath}";
//                            script.onload = function() {
//                                resolve(script)
//                            };
//                            script.onerror = function(event) {
//                                console.log("Error loading local Plotly JS from ${filePath}, using CDN instead")
//                                let div = document.createElement("div");
//                                div.style.color = 'darkred';
//                                div.textContent = 'Error loading local Plotly JS from ${filePath}, using CDN instead';
//                                element.appendChild(div);
//                                console.log("Loading plotly from CDN")
//                                let cdnScript = document.createElement("script");
//                                cdnScript.type = "text/javascript";
//                                cdnScript.src = "https://cdnjs.cloudflare.com/ajax/libs/plotly.js/1.54.6/plotly.min.js";
//                                cdnScript.onload = function() {
//                                    console.log("Successfully loaded Plotly from cdn")
//                                    resolve(cdnScript)
//                                };
//                                cdnScript.onerror = function(event){
//                                    console.log("Failed to load Plotly from cdn")
//                                    reject("Failed to load Plotly from cdn")
//                                }
//                                element.appendChild(cdnScript);
//                            };
//
//                            element.appendChild(script);
//                        });
//                    }
//
//                    """.trimIndent()
//                }
//            }
//        }
//    }

    /**
     * Embed plotly script into the jupyter notebook
     */
    fun embed(): HtmlVisitor = EmbededPlotlyJs

    /**
     * Use local system-wide storage for scripts
     */
    fun local(): HtmlVisitor = plotlyJupyterHeader

    /**
     * Use cdn script loader for plotly
     */
    fun cdn(): HtmlVisitor = PlotlyCdnHeader

    fun html(visitor: HtmlVisitor): String {
        val consumer = createHTML()
        visitor.visit(consumer)
        return consumer.finalize()
    }

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