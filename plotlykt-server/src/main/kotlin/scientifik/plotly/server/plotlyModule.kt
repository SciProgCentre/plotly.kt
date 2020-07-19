package scientifik.plotly.server

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.enum
import hep.dataforge.meta.long
import hep.dataforge.names.toName
import io.ktor.application.*
import io.ktor.features.CORS
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
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.html.*
import scientifik.plotly.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

enum class PlotlyUpdateMode {
    NONE,
    PUSH,
    PULL
}

class PlotlyServerConfig : Scheme() {
    var updateMode by enum(PlotlyUpdateMode.NONE, key = UPDATE_MODE_KEY)
    var updateInterval by long(300, key = UPDATE_INTERVAL_KEY)

    companion object : SchemeSpec<PlotlyServerConfig>(::PlotlyServerConfig) {
        val UPDATE_MODE_KEY = "update.mode".toName()
        val UPDATE_INTERVAL_KEY = "update.interval".toName()
    }
}


class PlotServerContainer(
    val baseUrl: Url,
    val controller: PlotlyPageController,
    val updateMode: PlotlyUpdateMode
) : PlotlyContainer {
    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
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

//class PlotlyServerPage(
//    val config: PlotlyServerPageConfig,
//    val route: String = "/",
//    val renderContent: FlowContent.(container: PlotlyContainer) -> Unit
//)

/**
 *
 */
fun Application.plotlyModule(pages: Map<String, PlotlyPage>, config: PlotlyServerConfig = PlotlyServerConfig.empty()) {
    if (featureOrNull(WebSockets) == null) {
        install(WebSockets)
    }

    if (featureOrNull(CORS) == null) {
        install(CORS) {
            anyHost()
        }
    }

    routing {
        static {
            resource("/js/plotly.min.js")
            resource("/js/plotly-push.js")
        }
        pages.forEach { (route, page) ->
            val controller = PlotlyPageController(this@plotlyModule, config.updateInterval)
            route(route) {

                //Update websocket
                webSocket("ws/{id}") {
                    //Use server-side filtering for specific page and plot if they are present in the request

                    val plotId: String? = call.parameters["id"] ?: error("Plot id not defined")

                    application.log.debug("Opened server socket for $plotId")

                    val subscription = controller.subscribe()
                    try {
                        subscription.consumeAsFlow().filter { it.id == plotId }.collect { update ->
                            if (update.id == plotId) {
                                val json = update.toJson()
                                outgoing.send(Frame.Text(json.toString()))
                            }
                        }
                    } catch (ex: Exception) {
                        application.log.debug("Closed server socket for $plotId")
                    } finally {
                        subscription.cancel()
                    }
                }
                //Plots in their json representation
                get("data/{id}") {
                    val id: String? = call.parameters["id"] ?: error("Plot id not defined")

                    val plot = controller.plots[id]
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
                                    attributes["onload"] = "window.promiseOfPlotlyPush = Promise.resolve()"
                                    type = "text/javascript"
                                    src = "/js/plotly-push.js"
                                }
                            }
                            title(page.title)
                        }
                        body {
                            val origin = call.request.origin
                            val url = URLBuilder().apply {
                                protocol = URLProtocol.createOrDefault(origin.scheme)
                                //workaround for https://github.com/ktorio/ktor/issues/1663
                                host = if (origin.host.startsWith("0:")) "[${origin.host}]" else origin.host
                                port = origin.port
                                encodedPath = origin.uri
                            }.build()
                            val container = PlotServerContainer(url, controller, config.updateMode)
                            with(page.fragment) {
                                render(container)
                            }
                        }
                    }

                }
            }
        }
    }
}

fun Application.plotlyModule(
    route: String = "/",
    config: PlotlyServerConfig = PlotlyServerConfig(),
    page: PlotlyPage
) {
    plotlyModule(mapOf(route to page), config)
}

fun Application.plotlyModule(
    route: String = "/",
    config: PlotlyServerConfig = PlotlyServerConfig(),
    title: String = "Plotly.kt",
    bodyBuilder: FlowContent.(container: PlotlyContainer) -> Unit
) {
    plotlyModule(route, config, Plotly.page(title = title, content = bodyBuilder))
}