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
import scientifik.plotly.PLOTLY_PROMISE_NAME
import scientifik.plotly.Plot2D
import scientifik.plotly.PlotlyConfig
import scientifik.plotly.PlotlyContainer

enum class PlotlyUpdateMode {
    NONE,
    PUSH,
    PULL
}

class PlotlyServerPageConfig : Scheme() {
    //TODO make separate title for different pages
    var title by string("Plotly.kt page")
    var updateMode by enum(PlotlyUpdateMode.NONE, key = "update.mode".toName())
    var updateInterval by long(300, key = UPDATE_INTERVAL_KEY)

    companion object {
        val UPDATE_INTERVAL_KEY = "update.interval".toName()
    }
}


class PlotServerContainer(val baseUrl: Url, val controller: PlotlyPageController, val updateMode: PlotlyUpdateMode): PlotlyContainer{
    override fun FlowContent.renderPlot(plot: Plot2D, plotId: String, config: PlotlyConfig): Plot2D {
        controller.listenTo(plot, plotId)
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
                            +"\n    startPull('$plotId', '$dataUrl', ${controller.updateInterval});\n"
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

class PlotlyServerPage(
    val config: PlotlyServerPageConfig,
    val route: String = "/",
    val renderContent: FlowContent.(container: PlotlyContainer) -> Unit
)

/**
 *
 */
fun Application.plotlyModule(pages: List<PlotlyServerPage>) {
    install(WebSockets){
        pingPeriodMillis = 3000
    }

    routing {
        static {
            resource("/js/plotly.min.js")
            resource("/js/updates.js")
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
                        call.respondText(plot.toJson().toString(), contentType = ContentType.Application.Json, status = HttpStatusCode.OK)
                    }
                }
                //filled pages
                get {
                    call.respondHtml {
                        head {
                            meta {
                                charset = "utf-8"
                                script {
                                    attributes["onload"] = "window.$PLOTLY_PROMISE_NAME = Promise.resolve(Plotly)"
                                    type = "text/javascript"
                                    src = "/js/plotly.min.js"
                                }
                                script {
                                    src = "/js/updates.js"
                                }
                            }
                            title(page.config.title)
                        }
                        body {
                            val origin = call.request.origin
                            val url = URLBuilder().apply {
                                protocol = URLProtocol.createOrDefault(origin.scheme)
                                //workaround for https://github.com/ktorio/ktor/issues/1663
                                host =  if(origin.host. startsWith("0:")) "[${origin.host}]" else origin.host
                                port = origin.port
                                encodedPath = origin.uri
                            }.build()
                            val container = PlotServerContainer(
                                url,
                                controller,
                                page.config.updateMode
                            )
                            with(page) {
                               renderContent(container)
                            }
                        }
                    }

                }
            }
        }
    }
}

fun Application.plotlyModule(
    config: PlotlyServerPageConfig,
    route: String = "/",
    contentRender: FlowContent.(container: PlotlyContainer) -> Unit
) {
    plotlyModule(listOf(PlotlyServerPage(config, route, contentRender)))
}