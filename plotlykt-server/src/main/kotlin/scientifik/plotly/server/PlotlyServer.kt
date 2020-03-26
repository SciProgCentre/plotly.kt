package scientifik.plotly.server

import hep.dataforge.meta.Config
import hep.dataforge.meta.get
import hep.dataforge.meta.scheme.Configurable
import hep.dataforge.meta.scheme.boolean
import hep.dataforge.meta.scheme.enum
import hep.dataforge.meta.scheme.number
import hep.dataforge.names.toName
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.newCoroutineContext
import kotlinx.html.*
import scientifik.plotly.*
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A simple Ktor server for displaying and updating plots
 */
@OptIn(ExperimentalCoroutinesApi::class, KtorExperimentalAPI::class)
class PlotlyServer(
    override val coroutineContext: CoroutineContext = GlobalScope.newCoroutineContext(EmptyCoroutineContext)
) : Configurable, CoroutineScope {

    override val config: Config = Config()

    enum class UpdateMode {
        NONE,
        PUSH,
        PULL
    }

    /**
     * Control dynamic updates via websocket
     */
    var updateMode by enum(UpdateMode.NONE, key = "update.mode".toName())
    var updateInterval by number(300, "update.interval".toName())
    var embedData by boolean(false)

    /**
     * A collection of all pages served by this server
     */
    private val pages = HashMap<String, PlotGrid>()

    private val updateChannel = BroadcastChannel<Update>(1)

    private var server: ApplicationEngine? = null

    /**
     * Start a server
     */
    fun start(host: String = "localhost", port: Int = 7777) {
        server = embeddedServer(io.ktor.server.cio.CIO, port, host) {
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(1)
            }
            routing {
                static {
                    resource("js/plots.js")
                }
                get("/") {
                    call.respondRedirect("/pages")
                }
                //Update websocket
                webSocket("/ws") {
                    //Use server-side filtering for specific page and plot if they are present in the request
                    val pageName = call.request.queryParameters["page"] ?: DEFAULT_PAGE
                    val plotName = call.request.queryParameters["plot"] ?: ""

                    val subscription = updateChannel.openSubscription()
                    try {
                        log.debug("Opened server socket for ${call.request.queryParameters}")

                        for (update in subscription) {
                            if (update.page == pageName && update.plot == plotName) {
                                val json = update.toJson()
                                outgoing.send(Frame.Text(json.toString()))
                            }
                        }
                    } catch (ex: Exception) {
                        subscription.cancel()
                        log.debug("Closed server socket for ${call.request.queryParameters}")
                    }
                }
                //Plots in their json representation
                get("/plots") {
                    val pageName = call.request.queryParameters["page"] ?: DEFAULT_PAGE
                    val plotName = call.request.queryParameters["plot"] ?: ""

                    val plot = pages[pageName]?.get(plotName)
                    if (plot == null) {
                        call.respond(HttpStatusCode.NotFound, "Plot $pageName/$plotName not found")
                    } else {
                        call.respondText(plot.toJson().toString(), contentType = ContentType.Application.Json)
                    }
                }
                //filled pages
                get("/pages/{page?}") {

                    val pageName = call.parameters["page"] ?: DEFAULT_PAGE
                    val page = pages[pageName]

                    if (page == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respondHtml {
                            val rows = page.cells.groupBy { it.rowNumber }.mapValues {
                                it.value.sortedBy { plot -> plot.colOrderNumber }
                            }.toList().sortedBy { it.first }

                            head {
                                meta {
                                    charset = "utf-8"
                                    script {
                                        src = "https://cdn.plot.ly/plotly-latest.min.js"
                                    }
                                    link(
                                        rel = "stylesheet",
                                        href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
                                    )
                                    script {
                                        src = "https://code.jquery.com/jquery-3.3.1.slim.min.js"
                                    }
                                    script {
                                        src =
                                            "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
                                    }
                                    script {
                                        src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
                                    }
                                    script {
                                        src = "/js/plots.js"
                                    }
                                }
                                title(page.title ?: "Untitled")
                            }
                            body {
                                plotGrid(rows)
                                rows.forEach { row ->
                                    row.second.mapIndexed { idx, cell ->
                                        val id = "${row.first}-$idx"
                                        val tracesParsed = cell.plot.data.toJsonString()
                                        val layoutParsed = cell.plot.layout.toJsonString()
                                        script {
                                            if (embedData) {
                                                unsafe {
                                                    +"""
                                                    createPlot(
                                                        '$id',
                                                        $tracesParsed,
                                                        $layoutParsed
                                                    );
    
                                                    """.trimIndent()
                                                }
                                            } else {
                                                val dataHost = host//call.request.host()
                                                val dataPort = port// call.request.port()
                                                val url =
                                                    "http://${dataHost}:${dataPort}/plots?page=$pageName&plot=${cell.plotId}"

                                                unsafe {
                                                    +"\n    createPlotFrom('$id','$url');\n"
                                                }

                                                // starting plot updates if required
                                                when (updateMode) {
                                                    UpdateMode.PUSH -> {
                                                        val updateHost = config["update.uri"]
                                                            ?: "ws://${dataHost.replace("http", "ws")}:${dataPort}/ws"
                                                        val query = "?page=$pageName&plot=${cell.plotId}"
                                                        unsafe {
                                                            +"\n    startPush('$id','$pageName', '${cell.plotId}','$updateHost$query');\n"
                                                        }
                                                    }
                                                    UpdateMode.PULL -> {
                                                        unsafe {
                                                            +"\n    startPull('$id','$url',${updateInterval.toLong()});\n"
                                                        }
                                                    }
                                                    UpdateMode.NONE -> {
                                                        //do nothing
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.also { it.start() }
    }

    /**
     * Gracefully stop the server
     */
    fun stop() {
        server?.stop(1000, 5000)
        server = null
    }

    private fun getPage(pageId: String): PlotGrid {
        return pages.getOrPut(pageId) {
            val listener = CollectingPageListener(this, updateChannel, pageId, updateInterval.toLong())
            listener.start()
            PlotGrid(listener).apply { title = "Plot page: $pageId" }
        }
    }

    /**
     * Generate a new page with plots
     */
    fun page(pageId: String = DEFAULT_PAGE, block: PlotGrid.() -> Unit) {
        getPage(pageId).apply(block)
    }

    /**
     * Add a plot to default page
     */
    fun plot(
        plot: Plot2D,
        rowNumber: Int = Int.MAX_VALUE,
        size: Int = 0,
        colOrderNumber: Int = Int.MAX_VALUE,
        plotId: String = plot.toString()
    ): Plot2D = getPage(DEFAULT_PAGE).plot(plot, rowNumber, size, colOrderNumber, plotId)

    fun plot(
        rowNumber: Int = Int.MAX_VALUE,
        size: Int = 0,
        colOrderNumber: Int = Int.MAX_VALUE,
        plotId: String? = null,
        block: Plot2D.() -> Unit
    ): Plot2D = getPage(DEFAULT_PAGE).plot(rowNumber, size, colOrderNumber, plotId, block)

    companion object {
        const val DEFAULT_PAGE = ""
    }
}

/**
 * Start static server (updates via reload)
 */
fun Plotly.serve(block: PlotlyServer.() -> Unit): PlotlyServer =
    PlotlyServer().apply(block).apply { start() }

/**
 * Configure server to start sending updates in push mode. Does not affect loaded pages
 */
fun PlotlyServer.pushUpdates(interval: Long = 100): PlotlyServer = apply {
    updateMode = PlotlyServer.UpdateMode.PUSH
    this.updateInterval = interval
}

/**
 * Configure client to request regular updates from server. Pull updates are more expensive than push updates since
 * they contain the full plot data and server can't decide what to send.
 */
fun PlotlyServer.pullUpdates(interval: Long = 1000): PlotlyServer = apply {
    updateMode = PlotlyServer.UpdateMode.PULL
    this.updateInterval = interval
}