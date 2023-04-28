package space.kscience.plotly.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.static
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.origin
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.html.*
import kotlinx.serialization.json.JsonObject
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name
import space.kscience.plotly.*
import space.kscience.plotly.server.PlotlyServer.Companion.DEFAULT_PAGE
import java.awt.Desktop
import java.net.URI
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext

public enum class PlotlyUpdateMode {
    NONE,
    PUSH,
    PULL
}

internal class ServerPlotlyRenderer(
    val baseUrl: Url,
    val updateMode: PlotlyUpdateMode,
    val updateInterval: Int,
    val embedData: Boolean,
    val plotCallback: (plotId: String, plot: Plot) -> Unit,
) : PlotlyRenderer {
    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        plotCallback(plotId, plot)
        div {
            id = plotId

            val dataUrl = URLBuilder(baseUrl).apply {
                encodedPath = baseUrl.encodedPath + "/data/$plotId"
            }.build()
            script {
                if (embedData) {
                    unsafe {
                        //language=JavaScript
                        +"""

                    makePlot(
                        '$plotId',
                        ${plot.data.toJsonString()},
                        ${plot.layout.toJsonString()},
                        $config
                    );


                    """.trimIndent()
                    }
                } else {
                    unsafe {
                        //language=JavaScript
                        +"\n    createPlotFrom('$plotId','$dataUrl', $config);\n"
                    }
                }

                // starting plot updates if required
                when (updateMode) {
                    PlotlyUpdateMode.PUSH -> {
                        val wsUrl = URLBuilder(baseUrl).apply {
                            protocol = URLProtocol.WS
                            encodedPath = baseUrl.encodedPath + "/ws/$plotId"
                        }.build()
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

public class PlotlyServer internal constructor(
    private val routing: Routing, private val rootRoute: String,
) : Configurable, CoroutineScope {

    override val coroutineContext: CoroutineContext get() = routing.application.coroutineContext

    override val meta: ObservableMutableMeta = MutableMeta()
    public var updateMode: PlotlyUpdateMode by meta.enum(PlotlyUpdateMode.PUSH, key = UPDATE_MODE_KEY)
    public var updateInterval: Int by meta.int(300, key = UPDATE_INTERVAL_KEY)
    public var embedData: Boolean by meta.boolean(false)

    /**
     * An override for data (pull/push) service host. By default uses request host
     */
    public var dataSourceHost: String? by meta.string()

    /**
     * An override for data (pull/push) service port. By default uses request port
     */
    public var dataSourcePort: Int? by meta.int()

    internal val root by lazy { routing.createRouteFromPath(rootRoute) }

    /**
     * a list of headers that should be applied to all pages
     */
    private val globalHeaders: ArrayList<PlotlyHtmlFragment> = ArrayList<PlotlyHtmlFragment>()

    public fun header(block: TagConsumer<*>.() -> Unit) {
        globalHeaders.add(PlotlyHtmlFragment(block))
    }

    internal fun Route.servePlotData(plots: Map<String, Plot>) {
        //Update websocket
        webSocket("ws/{id}") {
            val plotId: String = call.parameters["id"] ?: error("Plot id not defined")

            application.log.debug("Opened server socket for $plotId")

            val plot = plots[plotId] ?: error("Plot with id='$plotId' not registered")

            try {
                plot.collectUpdates(plotId, this, updateInterval).collect { update: Update ->
                    val json = update.toJson()
                    outgoing.send(Frame.Text(JsonObject(json).toString()))
                }
            } catch (ex: Exception) {
                application.log.debug("Closed server socket for $plotId")
            }
        }
        //Plots in their json representation
        get("data/{id}") {
            val id: String = call.parameters["id"] ?: error("Plot id not defined")

            val plot: Plot? = plots[id]
            if (plot == null) {
                call.respond(HttpStatusCode.NotFound, "Plot with id = $id not found")
            } else {
                call.respondText(
                    plot.toJsonString(),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.OK
                )
            }
        }
    }

    public fun page(
        plotlyFragment: PlotlyFragment,
        route: String = DEFAULT_PAGE,
        title: String = "Plotly server page '$route'",
        headers: List<PlotlyHtmlFragment> = emptyList(),
    ) {
        root.apply {
            val plots = HashMap<String, Plot>()
            route(route) {
                servePlotData(plots)
                //filled pages
                get {
                    val origin = call.request.origin
                    val url = URLBuilder().apply {
                        protocol = URLProtocol.createOrDefault(origin.scheme)
                        //workaround for https://github.com/ktorio/ktor/issues/1663
                        host = dataSourceHost ?: if (origin.serverHost.startsWith("0:")) "[${origin.serverHost}]" else origin.serverHost
                        port = dataSourcePort ?: origin.serverPort
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
                                (globalHeaders + headers).forEach {
                                    it.visit(consumer)
                                }
                                script {
                                    type = "text/javascript"
                                    src = "${normalizedRoute}js/plotly.min.js"
                                }
                                script {
                                    type = "text/javascript"
                                    src = "${normalizedRoute}js/plotlyConnect.js"
                                }
                            }
                            title(title)
                        }
                        body {
                            val container = ServerPlotlyRenderer(
                                url, updateMode, updateInterval, embedData
                            ) { plotId, plot ->
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

    public fun page(
        route: String = DEFAULT_PAGE,
        title: String = "Plotly server page '$route'",
        headers: List<PlotlyHtmlFragment> = emptyList(),
        content: FlowContent.(renderer: PlotlyRenderer) -> Unit,
    ) {
        page(PlotlyFragment(content), route, title, headers)
    }

    /**
     * Exposes the Ktor application environment to internal logic
     */
    public val application: Application get() = routing.application

    public companion object {
        public const val DEFAULT_PAGE: String = "/"
        public val UPDATE_MODE_KEY: Name = Name.parse("update.mode")
        public val UPDATE_INTERVAL_KEY: Name = Name.parse("update.interval")
    }
}


/**
 * Attach plotly application to given server
 */
public fun Application.plotlyModule(route: String = DEFAULT_PAGE, block: PlotlyServer.() -> Unit = {}): PlotlyServer {
    if (pluginOrNull(WebSockets) == null) {
        install(WebSockets)
    }

    routing {
        route(route) {
            static {
                resource("js/plotly.min.js")
                resource("js/plotlyConnect.js")
                //resources()
            }
        }
    }

//    val root: Route = feature(Routing).createRouteFromPath(route)
    return PlotlyServer(plugin(Routing), route).apply(block)
}


/**
 * Configure server to start sending updates in push mode. Does not affect loaded pages
 */
public fun PlotlyServer.pushUpdates(interval: Int = 100): PlotlyServer = apply {
    updateMode = PlotlyUpdateMode.PUSH
    updateInterval = interval
}

/**
 * Configure client to request regular updates from server. Pull updates are more expensive than push updates since
 * they contain the full plot data and server can't decide what to send.
 */
public fun PlotlyServer.pullUpdates(interval: Int = 500): PlotlyServer = apply {
    updateMode = PlotlyUpdateMode.PULL
    updateInterval = interval
}

/**
 * Start static server (updates via reload)
 */
@Suppress("ExtractKtorModule")
@OptIn(DelicateCoroutinesApi::class)
public fun Plotly.serve(
    scope: CoroutineScope = GlobalScope,
    host: String = "localhost",
    port: Int = 7777,
    block: PlotlyServer.() -> Unit,
): ApplicationEngine = scope.embeddedServer(io.ktor.server.cio.CIO, port, host) {
//    install(CallLogging)
    install(CORS) {
        anyHost()
    }

    plotlyModule(block = block)
}.start()

/**
 * A shortcut to make a single plot at the default page
 */
public fun PlotlyServer.plot(
    plotId: String? = null,
    config: PlotlyConfig = PlotlyConfig(),
    plotBuilder: Plot.() -> Unit,
) {
    page { plotly ->
        div {
            plot(plotId = plotId, config = config, renderer = plotly, builder = plotBuilder)
        }
    }
}

public fun ApplicationEngine.show() {
    val connector = environment.connectors.first()
    val uri = URI("http", null, connector.host, connector.port, null, null, null)
    Desktop.getDesktop().browse(uri)
}

public fun ApplicationEngine.close(): Unit = stop(1000, 5000)