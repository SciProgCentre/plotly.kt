package scientifik.plotly.server

import hep.dataforge.io.toJson
import hep.dataforge.meta.*
import hep.dataforge.names.Name
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.origin
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.port
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.html.*
import kotlinx.serialization.json.JsonObject
import scientifik.plotly.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * An update message for both data and layout
 */
private sealed class Update(val page: String, val plot: String) {
    abstract fun toJson(): JsonObject

    class Trace(page: String, plot: String, val trace: Int, val content: Meta) : Update(page, plot) {
        override fun toJson(): JsonObject = buildMeta {
            "page" to page
            "plot" to plot
            "contentType" to "trace"
            "trace" to trace
            "content" to content
        }.toJson()

    }

    class Layout(page: String, plot: String, val content: Meta) : Update(page, plot) {
        override fun toJson(): JsonObject = buildMeta {
            "page" to page
            "plot" to plot
            "contentType" to "layout"
            "content" to content
        }.toJson()
    }
}

/**
 * A simple Ktor serve for displaying and updating plots
 */
@KtorExperimentalAPI
@ExperimentalCoroutinesApi
class PlotlyServer(
    val meta: Meta = EmptyMeta,
    override val coroutineContext: CoroutineContext = EmptyCoroutineContext
) : CoroutineScope {

    /**
     * A collection of all pages served by this server
     */
    private val pages = HashMap<String, PlotGrid>()

    private val updateChannel = BroadcastChannel<Update>(1)

    private val server = embeddedServer(
        CIO,
        meta["port"].int ?: 7777,
        host = meta["host"].string ?: "localhost"
    ) {
        install(WebSockets)
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
                val pageName = call.request.queryParameters["page"]
                val plotName = call.request.queryParameters["plot"]

                val subscription = updateChannel.openSubscription()
                log.debug("Opened server socket for ${call.request.queryParameters}")

                launch {
                    closeReason.await()
                    subscription.cancel()
                    log.debug("Closed server socket for ${call.request.queryParameters}")
                }

                for (update in subscription) {
                    if ((pageName == null || update.page == pageName) && (plotName == null || update.plot == plotName)) {
                        val json = update.toJson()
                        send(Frame.Text(json.toString()))
                    }
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
                                    src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
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
                                        if (meta["embedData"].boolean == true) {
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
                                            val url =
                                                "http://${call.request.origin.host}:${call.request.port()}/plots?page=$pageName&plot=${cell.plotId}"
                                            unsafe { +"\ncreatePlotFrom('$id','$url');\n" }
                                        }
                                        // starting plot updates if required
                                        if (meta["update.enabled"].boolean == true) {
                                            val host = meta["update.uri"]
                                                ?: "ws://${call.request.origin.host.replace(
                                                    "http",
                                                    "ws"
                                                )}:${call.request.port()}/ws"
                                            val query = "?page=$pageName&plot=${cell.plotId}"
                                            unsafe { +"\nupdatePlot('$id','$pageName', '${cell.plotId}','$host$query');\n" }
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


    /**
     * A structure that collects changes to plot layout as well as trace configuration
     */
    private class PlotCollector {
        val layoutCollector = MetaChangeCollector()
        val traceCollectors = HashMap<Int, MetaChangeCollector>()

        fun getTrace(trace: Int) = traceCollectors.getOrPut(trace, ::MetaChangeCollector)
    }

    /**
     *
     */
    private inner class CollectingPageListener(val pageId: String, val interval: Long) : PageListener {

        private val collectors = HashMap<String, PlotCollector>()

        private var job: Job? = null

        private fun getCollector(plotId: String) = collectors.getOrPut(plotId, ::PlotCollector)

        override fun traceChanged(plotId: String, traceId: Int, itemName: Name, item: MetaItem<*>?) {
            launch {
                getCollector(plotId).getTrace(traceId).collect(itemName, item)
            }
        }

        override fun layoutChanged(plotId: String, itemName: Name, item: MetaItem<*>?) {
            launch {
                getCollector(plotId).layoutCollector.collect(itemName, item)
            }
        }

        /**
         * Start sending updates
         */
        fun start() {
            job = launch {
                while (isActive) {
                    delay(interval)
                    //checking all
                    collectors.forEach { (plotId, collector) ->
                        val layoutChange = collector.layoutCollector.read()
                        //send layout change if it is not empty
                        if (!layoutChange.isEmpty()) {
                            updateChannel.send(Update.Layout(pageId, plotId, layoutChange))
                        }
                        collector.traceCollectors.forEach { (traceNum, traceCollector) ->
                            val change = traceCollector.read()
                            //send trace change if it is not empty
                            if (!change.isEmpty()) {
                                updateChannel.send(Update.Trace(pageId, plotId, traceNum, change))
                            }
                        }
                    }
                }
            }
        }

        fun stop() {
            job?.cancel()
        }

    }

    /**
     * Start a server
     */
    fun start() {
        server.start()
    }

    /**
     * Gracefully stop the server
     */
    fun stop() {
        server.stop(1000, 5000, TimeUnit.MILLISECONDS)
    }

    private fun getPage(pageId: String): PlotGrid {
        return pages.getOrPut(pageId) {
            val listener = CollectingPageListener(pageId, meta["update.interval"].long ?: 100)
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
 * Start server using given meta
 */
@ExperimentalCoroutinesApi
@KtorExperimentalAPI
fun Plotly.serve(meta: Meta = EmptyMeta, block: PlotlyServer.() -> Unit): PlotlyServer =
    PlotlyServer(meta).apply(block).apply { start() }
