package scientifik.plotly.server

import hep.dataforge.meta.*
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
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.html.*
import scientifik.plotly.*
import java.awt.Desktop
import java.net.URI
import java.time.Duration
import kotlin.coroutines.CoroutineContext

/**
 * A simple Ktor server for displaying and updating plots
 */
@OptIn(ExperimentalCoroutinesApi::class, KtorExperimentalAPI::class, UnstablePlotlyAPI::class)
class PlotlyServer(
    parentScope: CoroutineScope = GlobalScope
) : Configurable, CoroutineScope {

    override val coroutineContext: CoroutineContext =
        parentScope.newCoroutineContext(Job(parentScope.coroutineContext[Job]))

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
    var updateInterval by number(300, key = UPDATE_INTERVAL_KEY)
    var embedData by boolean(false)
    var host by string("localhost")
    var port by int(7777)

    /**
     * A collection of all pages served by this server
     */
    private val pages = HashMap<String, PlotGrid>()

    private val updateChannel = BroadcastChannel<Update>(1)

    private var server: ApplicationEngine? = null

    /**
     * Start a server
     */
    fun start() {
        server = embeddedServer(io.ktor.server.cio.CIO, port, host) {
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(1)
            }
            routing {
                static {
                    resource("js/plots.js")
                    resource("js/plotly.min.js")
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

                    val plot = pages[pageName]?.get(plotName)?.plot
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
                            head {
                                meta {
                                    charset = "utf-8"
                                    script {
                                        src = "js/plotly.min.js"
                                    }
                                    script {
                                        src = "js/plots.js"
                                    }
                                }
                                title(page.title ?: "Untitled")
                            }
                            body {
                                if (embedData) {
                                    plotGrid(page)
                                }
                                page.grid.forEach { row ->
                                    row.forEach { cell ->
                                        val id = cell.id
                                        div {
                                            this.id = id
                                        }
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
                                                    "http://${dataHost}:${dataPort}/plots?page=$pageName&plot=${id}"

                                                unsafe {
                                                    +"\n    createPlotFrom('$id','$url');\n"
                                                }

                                                // starting plot updates if required
                                                when (updateMode) {
                                                    UpdateMode.PUSH -> {
                                                        val updateHost = config["update.uri"]
                                                            ?: "ws://${dataHost.replace("http", "ws")}:${dataPort}/ws"
                                                        val query = "?page=$pageName&plot=${id}"
                                                        unsafe {
                                                            +"\n    startPush('$id','$pageName', '$id','$updateHost$query');\n"
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
            config.onChange(listener) { name, oldItem, newItem ->
                if (name == UPDATE_INTERVAL_KEY) {
                    listener.interval = newItem.long ?: 300
                }
            }
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
        id: String = plot.toString(),
        width: Int = 6,
        row: Int? = null,
        col: Int? = null
    ): Plot2D = getPage(DEFAULT_PAGE).plot(plot, id, width, row, col)

    fun plot(
        row: Int? = null,
        col: Int? = null,
        id: String? = null,
        width: Int = 6,
        block: Plot2D.() -> Unit
    ): Plot2D = getPage(DEFAULT_PAGE).plot(row, col, id, width, block)

    fun show() {
        val uri = URI("http", null, host, port, null, null, null)
        Desktop.getDesktop().browse(uri)
    }


    companion object {
        const val DEFAULT_PAGE = ""
        val UPDATE_INTERVAL_KEY = "update.interval".toName()
    }
}

/**
 * Start static server (updates via reload)
 */
fun Plotly.serve(scope: CoroutineScope = GlobalScope, block: PlotlyServer.() -> Unit): PlotlyServer =
    PlotlyServer(scope).apply(block).apply { start() }

/**
 * Configure server to start sending updates in push mode. Does not affect loaded pages
 */
fun PlotlyServer.pushUpdates(interval: Long = 100): PlotlyServer = apply {
    updateMode = PlotlyServer.UpdateMode.PUSH
    updateInterval = interval
}

/**
 * Configure client to request regular updates from server. Pull updates are more expensive than push updates since
 * they contain the full plot data and server can't decide what to send.
 */
fun PlotlyServer.pullUpdates(interval: Long = 1000): PlotlyServer = apply {
    updateMode = PlotlyServer.UpdateMode.PULL
    updateInterval = interval
}