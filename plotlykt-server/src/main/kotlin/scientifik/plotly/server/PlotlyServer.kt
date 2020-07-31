package scientifik.plotly.server

import hep.dataforge.meta.Config
import hep.dataforge.meta.Configurable
import hep.dataforge.meta.enum
import hep.dataforge.meta.long
import hep.dataforge.names.toName
import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.features.origin
import io.ktor.html.respondHtml
import io.ktor.http.*
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import io.ktor.websocket.application
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.html.*
import scientifik.plotly.*
import scientifik.plotly.server.PlotlyServer.Companion.DEFAULT_PAGE
import java.awt.Desktop
import java.net.URI
import kotlin.collections.set

enum class PlotlyUpdateMode {
    NONE,
    PUSH,
    PULL
}

private class PlotServerContainer(
    val baseUrl: Url,
    val updateMode: PlotlyUpdateMode,
    val updateInterval: Long,
    val plotCallback: (plotId: String, plot: Plot) -> Unit
) : PlotlyContainer {
    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        plotCallback(plotId, plot)
        div {
            id = plotId

            val dataUrl = baseUrl.copy(
                encodedPath = baseUrl.encodedPath + "/data/$plotId"
            )
            script {
                unsafe {
                    //language=JavaScript
                    +"\n    createPlotFrom('$plotId','$dataUrl', $config);\n"
                }

                // starting plot updates if required
                when (updateMode) {
                    PlotlyUpdateMode.PUSH -> {
                        val wsUrl = baseUrl.copy(
                            protocol = URLProtocol.WS,
                            encodedPath = baseUrl.encodedPath + "/ws/$plotId"
                        )
                        unsafe {
                            //language=JavaScript
                            +"\n    startPush('$plotId', '$wsUrl');\n"
                        }
                    }
                    PlotlyUpdateMode.PULL -> {
                        unsafe {
                            //language=JavaScript
                            +"\n    startPull('$plotId', '$dataUrl', ${updateInterval});\n"
                        }
                    }
                    PlotlyUpdateMode.NONE -> {
                        //do nothing
                    }
                }
            }
        }
        return plot
    }

}

class PlotlyServer internal constructor(private val routing: Routing, private val rootRoute: String) : Configurable {
    override val config = Config()
    var updateMode by enum(PlotlyUpdateMode.NONE, key = UPDATE_MODE_KEY)
    var updateInterval by long(300, key = UPDATE_INTERVAL_KEY)

    /**
     * a list of headers that should be applied to all pages
     */
    val customHeaders = ArrayList<HtmlFragment>()

    fun header(block: TagConsumer<*>.() -> Unit) {
        customHeaders.add(HtmlFragment(block))
    }

    fun page(
        plotlyFragment: PlotlyFragment,
        route: String = DEFAULT_PAGE,
        title: String = "Plotly server page '$route'",
        headers: List<HtmlFragment> = customHeaders
    ) {
        routing.createRouteFromPath(rootRoute).apply {
            val plots = HashMap<String, Plot>()
            route(route) {
                //Update websocket
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
                //Plots in their json representation
                get("data/{id}") {
                    val id: String? = call.parameters["id"] ?: error("Plot id not defined")

                    val plot = plots[id]
                    if (plot == null) {
                        call.respond(HttpStatusCode.NotFound, "Plot with id = $id not found")
                    } else {
                        call.respondText(
                            plot.toJson().toString(),
                            contentType = ContentType.Application.Json,
                            status = HttpStatusCode.OK
                        )
                    }
                }
                //filled pages
                get {
                    val origin = call.request.origin
                    val url = URLBuilder().apply {
                        protocol = URLProtocol.createOrDefault(origin.scheme)
                        //workaround for https://github.com/ktorio/ktor/issues/1663
                        host = if (origin.host.startsWith("0:")) "[${origin.host}]" else origin.host
                        port = origin.port
                        encodedPath = origin.uri
                    }.build()
                    call.respondHtml {
                        val normalizedRoute = if (rootRoute.endsWith("/")) {
                            rootRoute
                        } else {
                            "$rootRoute/"
                        }

                        head {
                            meta {
                                charset = "utf-8"
                                headers.forEach {
                                    it.visit(consumer)
                                }
                                script {
                                    type = "text/javascript"
                                    src = "${normalizedRoute}js/plotly.min.js"
                                }
                                script {
                                    type = "text/javascript"
                                    src = "${normalizedRoute}js/plotly-server.js"
                                }
                            }
                            title(title)
                        }
                        body {
                            val container = PlotServerContainer(url, updateMode, updateInterval) { plotId, plot ->
                                plots[plotId] = plot
                            }
                            with(plotlyFragment) {
                                render(container)
                            }
                        }
                    }
                }
            }
        }
    }

    fun page(
        route: String = DEFAULT_PAGE,
        title: String = "Plotly server page '$route'",
        headers: List<HtmlFragment> = customHeaders,
        content: FlowContent.(container: PlotlyContainer) -> Unit
    ) {
        page(PlotlyFragment(content), route, title, headers)
    }


    companion object {
        const val DEFAULT_PAGE = "/"
        val UPDATE_MODE_KEY = "update.mode".toName()
        val UPDATE_INTERVAL_KEY = "update.interval".toName()
    }
}


/**
 * Attach plotly application to given server
 */
fun Application.plotlyModule(route: String = DEFAULT_PAGE): PlotlyServer {
    if (featureOrNull(WebSockets) == null) {
        install(WebSockets)
    }

    if (featureOrNull(CORS) == null) {
        install(CORS) {
            anyHost()
        }
    }


    routing {
        route(route) {
            static {
                resources()
            }
        }
    }

//    val root: Route = feature(Routing).createRouteFromPath(route)
    return PlotlyServer(feature(Routing), route)
}


/**
 * Configure server to start sending updates in push mode. Does not affect loaded pages
 */
fun PlotlyServer.pushUpdates(interval: Long = 100): PlotlyServer = apply {
    updateMode = PlotlyUpdateMode.PUSH
    updateInterval = interval
}

/**
 * Configure client to request regular updates from server. Pull updates are more expensive than push updates since
 * they contain the full plot data and server can't decide what to send.
 */
fun PlotlyServer.pullUpdates(interval: Long = 1000): PlotlyServer = apply {
    updateMode = PlotlyUpdateMode.PULL
    updateInterval = interval
}

/**
 * Start static server (updates via reload)
 */
@OptIn(KtorExperimentalAPI::class)
fun Plotly.serve(
    scope: CoroutineScope = GlobalScope,
    host: String = "localhost",
    port: Int = 7777,
    block: PlotlyServer.() -> Unit
): ApplicationEngine = scope.embeddedServer(io.ktor.server.cio.CIO, port, host) {
    plotlyModule().apply(block)
}.start()


fun ApplicationEngine.show() {
    val connector = environment.connectors.first()
    val uri = URI("http", null, connector.host, connector.port, null, null, null)
    Desktop.getDesktop().browse(uri)
}

fun ApplicationEngine.close() = stop(1000, 5000)