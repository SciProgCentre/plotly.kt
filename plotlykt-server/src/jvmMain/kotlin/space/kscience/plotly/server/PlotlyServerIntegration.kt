package space.kscience.plotly.server

import io.ktor.http.URLBuilder
import io.ktor.server.engine.ApplicationEngine
import kotlinx.html.div
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import org.slf4j.LoggerFactory
import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.boolean
import space.kscience.dataforge.meta.int
import space.kscience.plotly.*

public object PlotlyServerConfiguration : Scheme() {
    public var port: Int by int(System.getProperty("space.kscience.plotly.port")?.toInt() ?: 8882)
    public var updateInterval: Int by int(100)

    public var legacyMode: Boolean by boolean(false)

    /**
     * Switch plotly renderer to the legacy notebook mode (Jupyter classic)
     */
    public fun notebook(): PlotlyHtmlFragment {
        legacyMode = true
        return PlotlyHtmlFragment {
            div {
                style = "color: blue;"
                +"Plotly notebook integration switch into the legacy mode."
            }
        }
    }
}

internal val plotlyKtHeader = PlotlyHtmlFragment {
    script {
        src = "js/plotly-kt.js"
    }
}

/**
 * Global plotly jupyter configuration
 */
public val Plotly.jupyter: PlotlyServerConfiguration
    get() = PlotlyServerConfiguration

public class PlotlyServerIntegration : JupyterIntegration() {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var server: ApplicationEngine? = null

    private val plots = HashMap<String, Plot>()

    private var renderer: PlotlyRenderer = StaticPlotlyRenderer

    public val isServerStarted: Boolean get() = server != null


    private fun start(): PlotlyHtmlFragment = if (server != null) {
        PlotlyHtmlFragment {
            div {
                style = "color: blue;"
                +"The server is already running on ${Plotly.jupyter.port}. It must be shut down first to be restarted."
            }
        }
    } else {
        fun doStart(): PlotlyHtmlFragment {
            server?.stop(1000, 1000)
            server = Plotly.serve(host = "0.0.0.0", port = Plotly.jupyter.port) {
                root.servePlotData(plots)
            }
            val serverUrl = URLBuilder(port = Plotly.jupyter.port).build()
            renderer = ServerPlotlyRenderer(
                serverUrl, PlotlyUpdateMode.PUSH,
                Plotly.jupyter.updateInterval,
                true
            ) { plotId, plot ->
                plots[plotId] = plot
            }
            return PlotlyHtmlFragment {
                div {
                    style = "color: blue;"
                    +"Started plotly server on ${Plotly.jupyter.port}"
                }
            }
        }
        Plotly.jupyter.meta.onChange(this) { name ->
            if (name.toString() != PlotlyServerConfiguration::legacyMode.name) {
                logger.info("Plotly server config parameter $name changed. Restarting server.")
                doStart()
            }
        }
        logger.info("Starting Plotly-kt data server with ${Plotly.jupyter}")
        doStart()
    }

    private fun stop() {
        logger.info("Stopping Plotly-kt update server")
        server?.stop(1000, 1000)
        server = null
    }

    private fun renderPlot(plot: Plot): String = createHTML().div {
        plot(plot, config = PlotlyConfig {
            responsive = true
        }, renderer = renderer)
    }

    private fun renderFragment(fragment: PlotlyFragment): String = createHTML().div {
        with(fragment) {
            render(renderer)
        }
    }

    override fun Builder.onLoaded() {

        resources {
            js("plotly-kt") {
                classPath("js/plotly-kt.js")
            }
        }

        repositories("https://repo.kotlin.link")

        import(
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*",
            "kotlinx.coroutines.*"
        )

        import("space.kscience.plotly.server.jupyter")

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> { plot ->
            if (Plotly.jupyter.legacyMode) {
                HTML(
                    Plotly.page(plotlyKtHeader, renderer = renderer) {
                        plot(renderer = renderer, plot = plot)
                    }.render(), true
                )
            } else {
                HTML(renderPlot(plot))
            }
        }

        render<PlotlyFragment> { fragment ->
            if (Plotly.jupyter.legacyMode) {
                HTML(
                    Plotly.page(plotlyKtHeader, renderer = renderer) { renderer ->
                        fragment.render(this, renderer)
                    }.render(), true
                )
            } else {
                HTML(renderFragment(fragment))
            }
        }

        render<PlotlyPage> { page ->
            HTML(page.copy(headers = page.headers + plotlyKtHeader, renderer = renderer).render(), true)
        }

        onLoaded {
            logger.info("Starting Plotly-kt data server with ${Plotly.jupyter}")
            val serverStart = start()
            display(HTML(serverStart.toString()), null)
        }

        onShutdown {
            logger.info("Stopping Plotly-kt data server")
            Plotly.jupyter.meta.removeListener(this)
            display(HTML(stop().toString()), null)
        }
    }

}
