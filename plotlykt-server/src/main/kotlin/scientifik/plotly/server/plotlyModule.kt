package scientifik.plotly.server

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.enum
import hep.dataforge.meta.long
import hep.dataforge.meta.string
import hep.dataforge.names.toName
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.origin
import io.ktor.html.respondHtml
import io.ktor.http.*
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.application
import io.ktor.websocket.webSocket
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.html.*
import scientifik.plotly.Plot2D
import java.time.Duration

enum class PlotlyUpdateMode {
    NONE,
    PUSH,
    PULL
}

class PlotlyPageConfig : Scheme() {
    //TODO make separate title for different pages
    var title by string("Plotly.kt page")
    var updateMode by enum(PlotlyUpdateMode.NONE, key = "update.mode".toName())
    var updateInterval by long(300, key = UPDATE_INTERVAL_KEY)

    companion object {
        val UPDATE_INTERVAL_KEY = "update.interval".toName()
    }
}


class PlotServerContainer(val baseUrl: Url, val controller: PlotlyPageController, val updateMode: PlotlyUpdateMode)

/**
 * Attach plot with active data source to the element
 *
 * @param container an environment required to register plot data updates
 */
fun FlowContent.servePlot(
    container: PlotServerContainer,
    plot: Plot2D,
    plotId: String = plot.toString()
): Plot2D = with(container) {
    controller.listenTo(plot, plotId)
    div {
        id = plotId

        val dataUrl = baseUrl.copy(encodedPath = baseUrl.encodedPath + "/data/$plotId")
        script {
            unsafe {
                +"\n    createPlotFrom('$plotId','$dataUrl');\n"
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
                        +"\n    startPull('$plotId', '$dataUrl', ${controller.updateInterval});\n"
                    }
                }
                PlotlyUpdateMode.NONE -> {
                    //do nothing
                }
            }
        }
    }
    return@with plot
}

/**
 * Create a dynamic plot with pull or push data updates
 */
fun FlowContent.servePlot(
    container: PlotServerContainer,
    plotId: String? = null,
    plotBuilder: Plot2D.() -> Unit
): Plot2D {
    val plot = Plot2D().apply(plotBuilder)
    return servePlot(container, plot, plotId ?: plot.toString())
}

class PlotlyPage(
    val config: PlotlyPageConfig,
    val route: String = "/",
    val bodyBuilder: BODY.(container: PlotServerContainer) -> Unit
)

/**
 *
 */
fun Application.plotlyModule(pages: List<PlotlyPage>) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(1)
    }

    routing {
        static {
            resource("/js/plots.js")
            resource("/js/plotly.min.js")
        }
        pages.forEach { page ->
            val controller = PlotlyPageController(this@plotlyModule, page.config.updateInterval)
            route(page.route) {

                //Update websocket
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
                //Plots in their json representation
                get("data/{id}") {
                    val id: String? = call.parameters["id"] ?: error("Plot id not defined")

                    val plot = controller.plots[id]
                    if (plot == null) {
                        call.respond(HttpStatusCode.NotFound, "Plot with id = $id not found")
                    } else {
                        call.respondText(plot.toJson().toString(), contentType = ContentType.Application.Json)
                    }
                }
                //filled pages
                get {
                    call.respondHtml {
                        head {
                            meta {
                                charset = "utf-8"
                                script {
                                    src = "/js/plotly.min.js"
                                }
                                script {
                                    src = "/js/plots.js"
                                }
                            }
                            title(page.config.title)
                        }
                        body {
                            val origin = call.request.origin
                            val url = URLBuilder().apply {
                                protocol = URLProtocol.createOrDefault(origin.scheme)
                                host =  if(origin.host == "0:0:0:0:0:0:0:1") "localhost" else origin.host
                                port = origin.port
                                encodedPath = origin.uri
                            }.build()
                            val container = PlotServerContainer(
                                url,
                                controller,
                                page.config.updateMode
                            )
                            with(page) { bodyBuilder(container) }
                        }
                    }

                }
            }
        }
    }
}

fun Application.plotlyModule(
    config: PlotlyPageConfig,
    route: String = "/",
    bodyBuilder: BODY.(container: PlotServerContainer) -> Unit
) {
    plotlyModule(listOf(PlotlyPage(config, route, bodyBuilder)))
}