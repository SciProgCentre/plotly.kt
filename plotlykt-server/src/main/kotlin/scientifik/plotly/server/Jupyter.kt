package scientifik.plotly.server

import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CORS
import io.ktor.http.URLProtocol
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.util.url
import io.ktor.websocket.WebSockets
import io.ktor.websocket.application
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import scientifik.plotly.*
import java.nio.file.Path

private class PlotlyJupyterServer(
    val port: Int = 7777,
    updateInterval: Long = 50,
    parentScope: CoroutineScope = GlobalScope
) : PlotlyContainer, AutoCloseable {

    private val controller = PlotlyPageController(parentScope, updateInterval)

    val serverStarted = CompletableDeferred(Unit)

    private val server: ApplicationEngine = parentScope.embeddedServer(io.ktor.server.cio.CIO, port) {
        install(CORS) {
            anyHost()
        }
        install(WebSockets)
        routing {
            static {
                resource("/js/plotly.min.js")
                resource("/js/plotly-push.js")
            }
            webSocket("ws/{id}") {
                //Use server-side filtering for specific page and plot if they are present in the request

                val plotId: String? = call.parameters["id"] ?: error("Plot id not defined")

                try {
                    application.log.debug("Opened server socket for $plotId")

                    controller.updates().filter { it.id == plotId }.collect { update ->
                        if (update.id == plotId) {
                            val json = update.toJson()
                            outgoing.send(Frame.Text(json.toString()))
                        }
                    }
                } catch (ex: Exception) {
                    application.log.debug("Closed server socket for $plotId")
                }
            }
            serverStarted.complete(Unit)
        }
    }.start()

    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        controller.listenTo(plot, plotId)
        div {
            id = plotId
            script {
                val tracesString = plot.data.toJsonString()
                val layoutString = plot.layout.toJsonString()
                unsafe {
                    //language=JavaScript
                    +"""

                    window.$PLOTLY_PROMISE_NAME.then( plotly =>{
                        plotly.react(
                            '$plotId',
                            $tracesString,
                            $layoutString,
                            $config
                        );
                    });
                    
                    """.trimIndent()
                }
            }
            script {
                attributes["id"] = "$plotId-push"
                val wsUrl = url {
                    host = "localhost"
                    port = this@PlotlyJupyterServer.port
                    protocol = URLProtocol.WS
                    encodedPath = "/ws/$plotId"
                }
                unsafe {
                    //language=JavaScript
                    +"\n    startPush('$plotId', '$wsUrl');\n"
                }
            }
        }
        return plot
    }

    override fun close() {
        server.stop(1000, 5000)
    }
}


object Jupyter {
    const val JUPYTER_ASSETS_PATH = ".jupyter_kotlin/assets/"

    private val plotlyJupyterHeader = localScriptHeader(
        Path.of("."),
        Path.of(System.getProperty("user.home"))
            .resolve(JUPYTER_ASSETS_PATH)
            .resolve(PLOTLY_SCRIPT_PATH.removePrefix("/")),
        PLOTLY_SCRIPT_PATH
    )

    /**
     * Check if plotly loader is started and if not use url loader
     */
    private fun loadScripts(serverUrl: String) = HtmlFragment {
        script {
            val plotlyUrl = "$serverUrl/js/plotly.min.js"
            val pushUrl = "$serverUrl/js/plotly-push.js"
            unsafe {
                //language=JavaScript
                +"""
                if(typeof window.$PLOTLY_PROMISE_NAME === 'undefined'){
                    console.log("Plotly loader is not defined. Loading from $plotlyUrl.")
                    let plotlyPromise = new Promise( (resolve, reject) => {
                        let plotlyScript = document.createElement("script");
                        plotlyScript.type = "text/javascript";
                        plotlyScript.src = "$plotlyUrl";
                        plotlyScript.onload = function() {
                            console.log("Successfully loaded Plotly from $plotlyUrl")
                            resolve(Plotly)
                        };
                        plotlyScript.onerror = function(event){
                            console.log("Failed to load Plotly from $plotlyUrl")
                            reject("Failed to load Plotly from $plotlyUrl")
                        }
                        document.body.appendChild(plotlyScript);
                    });  
                    
                    let pushPromise = new Promise( resolve => {
                        let pushScript = document.createElement("script");
                        pushScript.type = "text/javascript";
                        pushScript.src = "$pushUrl";
                        pushScript.onload = function() {
                            resolve()
                        };
                        pushScript.onerror = function(event){
                            console.log("Failed to load Plotly-push from $pushUrl")
                            reject("Failed to load Plotly-push from $pushUrl")
                        }                        
                        document.body.appendChild(pushScript);
                    });
                    
                    window.$PLOTLY_PROMISE_NAME = Promise.all([plotlyPromise, pushPromise])
                        .then(values => values[0])
                        .catch(reason => window.$PLOTLY_PROMISE_NAME = undefined)
                }
                    
            """.trimIndent()
            }
        }
    }

    /**
     * Embed plotly script into the jupyter notebook
     */
    fun embedPlotly() = embededPlotlyHeader

    /**
     * Use local system-wide storage for scripts
     */
    fun localPlotly() = plotlyJupyterHeader

    /**
     * Use cdn script loader for plotly
     */
    fun cdnPlotly() = cdnPlotlyHeader

    @UnstablePlotlyAPI
    fun useMathJax() = HtmlFragment {
        script {
            type = "text/javascript"
            unsafe {
                //language=JavaScript
                +"""
                    window.PlotlyConfig = {MathJaxConfig: 'local'};
                """.trimIndent()
            }
        }
        script {
            type = "text/javascript"
            async = true
            src = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-MML-AM_SVG"
        }
        script {
            unsafe {
                //language=JavaScript
                +"if (window.MathJax) MathJax.Hub.Config({SVG: {font: \"STIX-Web\"}});"
            }
        }
    }

    private var jupyterPlotlyServer: PlotlyJupyterServer? = null

    private val port = 8882

    fun startServer(updateInterval: Long = 50): HtmlFragment {
        jupyterPlotlyServer = PlotlyJupyterServer(port = port, updateInterval = updateInterval).also {
            runBlocking {
                it.serverStarted.await()
            }
        }
        return loadScripts("http://localhost:$port")
    }

    fun stopServer() {
        jupyterPlotlyServer?.close()
    }

    private val jupyterPlotlyContainer = object : PlotlyContainer {
        override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
            loadScripts("http://localhost:$port").visit(consumer)
            //choose dynamic or static rendering depending on what is active
            return (jupyterPlotlyServer ?: StaticPlotlyContainer).run { renderPlot(plot, plotId, config) }
        }
    }

    fun renderPlot(plot: Plot): String = createHTML().div {
        plot(plot, config = PlotlyConfig {
            responsive = true
        }, container = jupyterPlotlyContainer)
    }

    fun renderFragment(fragment: PlotlyFragment): String = createHTML().div {
        with(fragment) {
            render(jupyterPlotlyContainer)
        }
    }

    fun renderPage(page: PlotlyPage): String = page.copy(container = jupyterPlotlyContainer).render()
}