package space.kscience.plotly.server

import io.ktor.http.URLBuilder
import io.ktor.server.engine.ApplicationEngine
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import org.slf4j.LoggerFactory
import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.int
import space.kscience.plotly.*
import space.kscience.plotly.Plotly.PLOTLY_CDN

public class PlotlyServerConfig() : Scheme() {
    public var port: Int by int(System.getProperty("space.kscience.plotly.port")?.toInt() ?: 8882)
    public var updateInterval: Int by int(100)
}

@JupyterLibrary
internal class PlotlyServerIntegration : JupyterIntegration() {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var server: ApplicationEngine? = null

    private val plots = HashMap<String, Plot>()

    private var renderer: PlotlyRenderer = StaticPlotlyRenderer

    private fun start(config: PlotlyServerConfig): PlotlyHtmlFragment = if (server != null) {
        PlotlyHtmlFragment {
            div {
                style = "color: blue;"
                +"The server is already running on ${config.port}. It must be shut down first to be restarted."
            }
        }
    } else {
        fun doStart(): PlotlyHtmlFragment {
            server = Plotly.serve(port = config.port) {
                root.servePlotData(plots)
            }
            val serverUrl = URLBuilder(port = config.port).build()
            renderer = ServerPlotlyRenderer(
                serverUrl, PlotlyUpdateMode.PUSH,
                config.updateInterval,
                true
            ) { plotId, plot ->
                plots[plotId] = plot
            }
            return PlotlyHtmlFragment {
                div {
                    style = "color: blue;"
                    +"Started plotly server on ${config.port}"
                }
            }
        }
        config.onChange(this) { name, oldItem, newItem ->
            if (oldItem != newItem) {
                logger.info("Plotly server config parameter $name changed to $newItem")
                doStart()
            }
        }
        doStart()
    }

    private fun stop() {
        server?.stop(1000, 2000)
        server = null
    }

    @UnstablePlotlyAPI
    override fun Builder.onLoaded() {

        resources {
            js("plotly") {
                url(PLOTLY_CDN)
                classPath("js/plotly.min.js")
            }
            js("plotlyConnect") {
                classPath("js/plotlyConnect.js")
            }
        }

        repositories("https://repo.kotlin.link")

        import(
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*"
        )

        import<PlotlyServerConfig>()

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> {
            HTML(
                createHTML().div {
                    renderer.run { renderPlot(it) }
                }
            )
        }

        render<PlotlyFragment> { fragment ->
            HTML(
                createHTML().div {
                    fragment.render(this, renderer)
                }
            )
        }

        render<PlotlyPage> { page ->
            HTML(page.copy(renderer = renderer).render(), true)
        }

        onLoaded {
            val config = execute("val plotly = PlotlyServerConfig(); plotly;").value as PlotlyServerConfig
            logger.info("Starting JupyterPlotlyServer with $config")
            val serverStart = start(config)
            logger.info("JupyterPlotlyServer started with $config")
            display(HTML(serverStart.toString()))
        }

        onShutdown {
            logger.info("Stopping JupyterPlotlyServer")
            display(HTML(stop().toString()))
        }
    }

}
