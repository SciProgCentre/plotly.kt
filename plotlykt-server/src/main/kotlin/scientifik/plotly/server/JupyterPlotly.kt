package scientifik.plotly.server

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CORS
import io.ktor.http.URLProtocol
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.url
import io.ktor.websocket.WebSockets
import io.ktor.websocket.application
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import scientifik.plotly.*

private class PlotlyJupyterServer(
    val updateInterval: Long = 50,
    val parentScope: CoroutineScope = GlobalScope
) : PlotlyContainer {

    var port: Int = 8882
        private set

    var isRunning: Boolean = false
        private set


    private var server: ApplicationEngine? = null

    private val plots = HashMap<String, Plot>()

    @OptIn(KtorExperimentalAPI::class)
    suspend fun start(port: Int = 8882) {
        this.port = port
        server = parentScope.embeddedServer(io.ktor.server.cio.CIO, port) {
            install(CORS) {
                anyHost()
            }
            install(WebSockets)
            routing {
                static {
                    resources()
                }
                webSocket("ws/{id}") {
                    val plotId: String = call.parameters["id"] ?: error("Plot id not defined")

                    application.log.debug("Opened server socket for $plotId")

                    val plot = plots[plotId] ?: error("Plot with id='$plotId' not registered")

                    try {
                        plot.collectUpdates(plotId, this, updateInterval).collect { update ->
                            val json = update.toJson()
                            outgoing.send(Frame.Text(json.toString()))
                        }
                    } catch (ex: Exception) {
                        application.log.debug("Closed server socket for $plotId")
                    }
                }
            }
        }.start().also {
            val deferred = CompletableDeferred<Application>()
            it.environment.monitor.subscribe(ApplicationStarted) {
                deferred.complete(it)
            }
            deferred.join()
        }
        isRunning = true
    }

    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        plots[plotId] = plot
        div {
            id = plotId
            script {
                unsafe {
                    //language=JavaScript
                    +"""
                        window.plotlyCall(function (){                    
                            makePlot(
                                '$plotId',
                                ${plot.data.toJsonString()},
                                ${plot.layout.toJsonString()},
                                $config
                            );
                        });
                    
                    """.trimIndent()
                }
            }
            if (isRunning) {
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
                        +"""
                            window.plotlyCall(function (){        
                                startPush('$plotId', '$wsUrl');
                            });    
                            
                        """
                    }
                }
            }
        }
        return plot
    }

    fun stop() {
        isRunning = false
        server?.stop(1000, 5000)
    }
}

@UnstablePlotlyAPI
object JupyterPlotly {
    private const val JUPYTER_ASSETS_PATH = ".jupyter_kotlin/assets/"

    private fun loadJs(serverUrl: String) = HtmlFragment {
        div {
            id = "plotly-load-scripts"
        }
        script {
            type = "text/javascript"
            unsafe {
                //language=JavaScript
                +"""
                (function() {
                    console.log("Starting up plotly script loader");
                    if (window.MathJax){ 
                        MathJax.Hub.Config({
                            SVG: {
                                font: "STIX-Web"
                            }
                        });
                        window.PlotlyConfig = {MathJaxConfig: 'local'};
                    }
                    
                    if(!window.plotlyCallQueue) {
                        window.plotlyCallQueue = [];
                    }
                    
                    window.plotlyCall = function(f) {
                        window.plotlyCallQueue.push(f);
                    };
                    
                    window.startupPlotly = function (){
                        console.info("Starting up plotly and calling deferred operations stack.")
                        window.plotlyCall = function(f) {f();};
                        window.plotlyCallQueue.forEach(function(f) {f();});
                        window.plotlyCallQueue = [];    
                    }
                })();
                """.trimIndent()
            }
        }

        script {
            type = "text/javascript"
            src = "$serverUrl/js/plotly.min.js"
        }

        script {
            type = "text/javascript"
            val connectorScript = javaClass.getResource("/js/plotlyConnect.js")!!.readText()
            unsafe {
                +connectorScript
            }
            unsafe {
                //language=JavaScript
                +"""
                    window.startupPlotly()
                """.trimIndent()
            }
        }
    }

    private var jupyterPlotlyServer: PlotlyJupyterServer = PlotlyJupyterServer()

    /**
     * Start a dynamic update server
     */
    fun start(port: Int = 8882): HtmlFragment {
        if (jupyterPlotlyServer.isRunning) {
            return HtmlFragment {
                div {
                    style = "color: blue;"
                    +"The server is already running on ${jupyterPlotlyServer.port}. It must be shut down first to be restarted."
                }
            }
        }
        runBlocking { jupyterPlotlyServer.start(port) }
        return loadJs("//localhost:$port")
    }

    /**
     * Stop dynamic update server
     */
    fun stop(): HtmlFragment {
        if (!jupyterPlotlyServer.isRunning) {
            return HtmlFragment {
                div {
                    +"Update server is not running"
                }
            }
        }
        runBlocking { jupyterPlotlyServer.stop() }
        return HtmlFragment {
            div {
                +"Update server is stopped"
            }
        }
    }

    fun renderPlot(plot: Plot): String = createHTML().div {
        plot(plot, config = PlotlyConfig {
            responsive = true
        }, container = jupyterPlotlyServer)
    }

    fun renderFragment(fragment: PlotlyFragment): String = createHTML().div {
        with(fragment) {
            render(jupyterPlotlyServer)
        }
    }

    fun renderPage(page: PlotlyPage): String = page.copy(container = jupyterPlotlyServer).render()
}