package space.kscience.plotly.server

import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CORS
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.websocket.WebSockets
import io.ktor.websocket.application
import io.ktor.websocket.webSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import org.slf4j.LoggerFactory
import space.kscience.plotly.*
import space.kscience.plotly.Plotly.PLOTLY_CDN

@JupyterLibrary
internal class PlotlyServerIntegration : JupyterIntegration() {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var port = System.getProperty("space.kscience.plotly.port")?.toInt() ?: 8882
    private var updateInterval = 50

    private var server: ApplicationEngine? = null

    private val plots = HashMap<String, Plot>()

    private val renderer: PlotlyRenderer = StaticPlotlyRenderer

//    private fun loadJs(serverUrl: String) = PlotlyHtmlFragment {
//
//        script {
//            type = "text/javascript"
//            unsafe {
//                //language=JavaScript
//                +"""
//                (function() {
//                    console.log("Starting up plotly script loader");
//                    //initialize LaTeX for Jupyter
//                    window.PlotlyConfig = {MathJaxConfig: 'local'};
//                    window.startupPlotly = function (){
//                        if (window.MathJax){
//                            MathJax.Hub.Config({
//                                SVG: {
//                                    font: "STIX-Web"
//                                }
//                            });
//                        }
//                        console.info("Calling deferred operations in Plotly queue.")
//                        if(window.plotlyCallQueue){
//                            window.plotlyCallQueue.forEach(function(theCall) {theCall();});
//                            window.plotlyCallQueue = [];
//                        }
//                    }
//                })();
//                """.trimIndent()
//            }
//        }
//
//        script {
//            type = "text/javascript"
//            src = "$serverUrl/js/plotly.min.js"
//        }
//
//        script {
//            type = "text/javascript"
//            val connectorScript = javaClass.getResource("/js/plotlyConnect.js")!!.readText()
//            unsafe {
//                +connectorScript
//            }
//            unsafe {
//                //language=JavaScript
//                +"window.startupPlotly()"
//            }
//        }
//    }

    private fun start(): PlotlyHtmlFragment {
        return if (server != null) {
            PlotlyHtmlFragment {
                div {
                    style = "color: blue;"
                    +"The server is already running on ${port}. It must be shut down first to be restarted."
                }
            }
        } else {
            runBlocking {
                server = embeddedServer(
                    io.ktor.server.cio.CIO,
                    port,
                    parentCoroutineContext = Dispatchers.Default
                ) {
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
                }.start(false)
            }
            PlotlyHtmlFragment {
                div {
                    style = "color: blue;"
                    +"Started plotly server on ${port}"
                }
            }
        }

    }

//    fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
//        plots[plotId] = plot
//        div {
//            id = plotId
//            script {
//                unsafe {
//                    //language=JavaScript
//                    +"""
//                        if(typeof Plotly !== "undefined"){
//                            makePlot(
//                                    '$plotId',
//                                    ${plot.data.toJsonString()},
//                                    ${plot.layout.toJsonString()},
//                                    ${config.toJsonString()}
//                            );
//                        } else {
//                            console.error("Plotly not loaded")
//                        }
//                    """.trimIndent()
//                }
//            }
//            if (server?.application?.isActive == true) {
//                script {
//                    attributes["id"] = "$plotId-push"
//                    val wsUrl = url {
//                        host = "localhost"
//                        port = this@PlotlyServerIntegration.port
//                        protocol = URLProtocol.WS
//                        encodedPath = "/ws/$plotId"
//                    }
//                    unsafe {
//                        //language=JavaScript
//                        +"""
//
//                            startPush('$plotId', '$wsUrl');
//
//                        """
//                    }
//                }
//            }
//        }
//        return plot
//    }

    private fun stop() {
        server?.stop(1000, 5000)
    }


    @UnstablePlotlyAPI
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

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> {
            HTML(
                renderer.run {
                    createHTML().div {
                        renderPlot(it)
                    }
                }
            )
        }

        render<PlotlyFragment> { fragment ->
            HTML(
                createHTML().div {
                    fragment.render(this, renderer)
                }
            )
        }

        render<PlotlyPage> { page ->
            HTML(page.copy(renderer = renderer).render(), true)
        }

        onLoaded {
            logger.info("Starting JupyterPlotlyServer at $port")
            val serverStart = start()
            logger.info("JupyterPlotlyServer started at $port")
            display(HTML(serverStart.toString()))
        }

        onShutdown {
            logger.info("Stopping JupyterPlotlyServer")
            display(HTML(stop().toString()))
        }
    }

}
