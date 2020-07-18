package scientifik.plotly.server

import hep.dataforge.names.toName
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import kotlinx.html.FlowContent
import scientifik.plotly.Plotly
import scientifik.plotly.PlotlyContainer
import scientifik.plotly.UnstablePlotlyAPI
import java.awt.Desktop
import java.net.URI


/**
 * A simple Ktor server for displaying and updating plots
 */
@OptIn(ExperimentalCoroutinesApi::class, KtorExperimentalAPI::class, UnstablePlotlyAPI::class)
class PlotlyServer(
    val parentScope: CoroutineScope = GlobalScope,
    val host: String = "localhost",
    val port: Int = 7777
) : AutoCloseable {

    val config: PlotlyServerPageConfig = PlotlyServerPageConfig()

    private val serverStarterWaiter = CompletableDeferred<Unit>()

    private val server: ApplicationEngine = parentScope.embeddedServer(io.ktor.server.cio.CIO, port, host) {
        install(CORS) {
            anyHost()
        }
        routing {
            //Need to wait for routing to start
            serverStarterWaiter.complete(Unit)
        }
    }.start()

    /**
     * Generate a new page with plots
     */
    fun page(route: String = DEFAULT_PAGE, bodyBuilder: FlowContent.(container: PlotlyContainer) -> Unit) {
        parentScope.launch {
            serverStarterWaiter.join()
            server.application.plotlyModule(config, route, bodyBuilder)
        }
    }


//
//    /**
//     * Add a plot to default page
//     */
//    fun plot(
//        plot: Plot2D,
//        id: String = plot.toString(),
//        width: Int = 6,
//        row: Int? = null,
//        col: Int? = null
//    ): Plot2D = getPage(DEFAULT_PAGE).plot(plot, id, width, row, col)
//
//    fun plot(
//        row: Int? = null,
//        col: Int? = null,
//        id: String? = null,
//        width: Int = 6,
//        block: Plot2D.() -> Unit
//    ): Plot2D = getPage(DEFAULT_PAGE).plot(row, col, id, width, block)

    fun show() {
        val uri = URI("http", null, host, port, null, null, null)
        Desktop.getDesktop().browse(uri)
    }


    override fun close() {
        server.stop(1000, 5000)
    }

    companion object {
        const val DEFAULT_PAGE = "/"
        val UPDATE_INTERVAL_KEY = "update.interval".toName()
    }
}

/**
 * Start static server (updates via reload)
 */
fun Plotly.serve(
    scope: CoroutineScope = GlobalScope,
    host: String = "localhost",
    port: Int = 7777,
    block: PlotlyServer.() -> Unit
): PlotlyServer = PlotlyServer(scope, host, port).apply(block)

/**
 * Configure server to start sending updates in push mode. Does not affect loaded pages
 */
fun PlotlyServer.pushUpdates(interval: Long = 100): PlotlyServer = apply {
    config.updateMode = PlotlyUpdateMode.PUSH
    config.updateInterval = interval
}

/**
 * Configure client to request regular updates from server. Pull updates are more expensive than push updates since
 * they contain the full plot data and server can't decide what to send.
 */
fun PlotlyServer.pullUpdates(interval: Long = 1000): PlotlyServer = apply {
    config.updateMode = PlotlyUpdateMode.PULL
    config.updateInterval = interval
}