package scientifik.plotly.server

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
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
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import scientifik.plotly.*
import java.nio.file.Path

private class PlotlyJupyterServer(
    val updateInterval: Long = 50,
    val parentScope: CoroutineScope = GlobalScope
) : PlotlyContainer {

    var port: Int = 8882
        private set

    var isRunning: Boolean = false
        private set


    private var server: ApplicationEngine? = null

    private val plots = HashMap<String,Plot>()

    suspend fun start(port: Int = 8882) {
        this.port = port
        server = parentScope.embeddedServer(io.ktor.server.cio.CIO, port) {
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
                        +"\n    startPush('$plotId', '$wsUrl');\n"
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
object Jupyter {
    private const val JUPYTER_ASSETS_PATH = ".jupyter_kotlin/assets/"

    private val plotlyJupyterHeader = localScriptHeader(
        Path.of("."),
        Path.of(System.getProperty("user.home"))
            .resolve(JUPYTER_ASSETS_PATH)
            .resolve(PLOTLY_SCRIPT_PATH.removePrefix("/")),
        PLOTLY_SCRIPT_PATH
    )

    fun useLocalPlotly() = plotlyJupyterHeader

    fun useCdnPlotly() = cdnPlotlyHeader

//    fun useCdnPlotly() = HtmlFragment {
//        div {
//            id = "plotly_cdn_loader"
//            script {
//                type = "text/javascript"
//                //language=JavaScript
//                +"""
//                if(typeof window.$PLOTLY_PROMISE_NAME === 'undefined'){
//                    console.log("Plotly loader is not defined. Loading from $PLOTLY_CDN.")
//                    window.$PLOTLY_PROMISE_NAME = new Promise( (resolve, reject) => {
//                        let plotlyScript = document.createElement("script");
//                        plotlyScript.type = "text/javascript";
//                        plotlyScript.src = "$PLOTLY_CDN";
//                        plotlyScript.onload = function() {
//                            console.log("Successfully loaded Plotly from $PLOTLY_CDN");
//                            resolve(Plotly);
//                        };
//                        plotlyScript.onerror = function(event){
//                            console.log("Failed to load Plotly from $PLOTLY_CDN");
//                            reject("Failed to load Plotly from $PLOTLY_CDN");
//                            window.$PLOTLY_PROMISE_NAME = undefined;
//                            let errorDiv = document.createElement("div");
//                            errorDiv.style.color = "darkred";
//                            errorDiv.textContent = "Failed to load Plotly from CDN try using Jupyter.startUpdates() to obtain local copy.";
//                            document.getElementById("plotly_cdn_loader").appendChild(errorDiv);
//                        }
//                        document.body.appendChild(plotlyScript);
//                    });
//                }
//                """.trimIndent()
//            }
//        }
//    }

    fun usePlotlyForClassicNotebook() = HtmlFragment {
        script {
            type = "text/javascript"
            unsafe {
                //language=JavaScript
                +"""
                    require.config({ 
                         paths: { 
                            "plotly_notebook": '$PLOTLY_CDN'
                         }
                    });
                    window.$PLOTLY_PROMISE_NAME = new Promise( (resolve) =>{
                        require(['plotly_notebook'], (plotly) => {
                            resolve(plotly);
                            console.log(plotly);
                        });
                    });
                """.trimIndent()
            }
        }
    }

    /**
     * Check if plotly loader is started and if not use url loader
     */
    private fun loadServerScripts(serverUrl: String) = HtmlFragment {
        script {
            type = "text/javascript"
            val plotlyUrl = "$serverUrl/js/plotly.min.js"
            unsafe {
                //language=JavaScript
                +"""
                if(typeof window.$PLOTLY_PROMISE_NAME === 'undefined'){
                    console.log("Plotly loader is not defined. Loading from $plotlyUrl.");
                    window.$PLOTLY_PROMISE_NAME = new Promise( (resolve, reject) => {
                        let plotlyScript = document.createElement("script");
                        plotlyScript.type = "text/javascript";
                        plotlyScript.src = "$plotlyUrl";
                        plotlyScript.onload = function() {
                            console.log("Successfully loaded Plotly from $plotlyUrl");
                            resolve(Plotly);
                        };
                        plotlyScript.onerror = function(event){
                            console.log("Failed to load Plotly from $plotlyUrl")
                            reject("Failed to load Plotly from $plotlyUrl");
                            window.$PLOTLY_PROMISE_NAME = undefined;
                        }
                        document.body.appendChild(plotlyScript);
                    });  
                }
                """.trimIndent()
            }
        }
        script {
            type = "text/javascript"
            val pushUrl = "$serverUrl/js/plotly-push.js"
            unsafe {
                //language=JavaScript
                +"""
                if(typeof window.promiseOfPlotlyPush === 'undefined'){
                    window.promiseOfPlotlyPush = new Promise( resolve => {
                        let pushScript = document.createElement("script");
                        pushScript.type = "text/javascript";
                        pushScript.src = "$pushUrl";
                        pushScript.onload = function() {
                            console.log("Plotly push sctipts are loaded")
                            resolve();
                        };
                        pushScript.onerror = function(event){
                            console.log("Failed to load Plotly-push from $pushUrl");
                            reject("Failed to load Plotly-push from $pushUrl");
                            window.promiseOfPlotlyPush = undefined;
                        }                        
                        document.body.appendChild(pushScript);
                    });
                }
                    
            """.trimIndent()
            }
        }
    }

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

    private var jupyterPlotlyServer: PlotlyJupyterServer = PlotlyJupyterServer()

    /**
     * Start a dynamic update server
     */
    fun startUpdates(port: Int = 8882): HtmlFragment {
        if (jupyterPlotlyServer.isRunning) {
            return HtmlFragment {
                div {
                    style = "color: \"darkblue\""
                    +"The server is already running on ${jupyterPlotlyServer.port}. It must be shut down first to be restarted."
                }
            }
        }
        runBlocking { jupyterPlotlyServer.start(port) }
        return loadServerScripts("http://localhost:$port")
    }

    /**
     * Stop dynamic update server
     */
    fun stopUpdates(): HtmlFragment {
        if (!jupyterPlotlyServer.isRunning) {
            return HtmlFragment {
                div {
                    +"Update server is not running"
                }
            }
        }
        runBlocking { jupyterPlotlyServer.stop() }
        return HtmlFragment {
            script {
                unsafe {
                    //language=JavaScript
                    +"""
                    window.$PLOTLY_PROMISE_NAME = undefined
                    window.promiseOfPlotlyPush = undefined
                    """.trimIndent()
                }
            }
            div {
                +"Update server is stopped script headers are reset"
            }
            cdnPlotlyHeader.visit
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