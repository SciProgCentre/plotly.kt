package space.kscience.plotly.server

import io.ktor.http.URLBuilder
import io.ktor.server.engine.ApplicationEngine
import kotlinx.html.div
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.unsafe
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import org.slf4j.LoggerFactory
import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.boolean
import space.kscience.dataforge.meta.int
import space.kscience.plotly.*
import space.kscience.plotly.Plotly.PLOTLY_CDN

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

private val plotlyConnectHeader = PlotlyHtmlFragment {
    script {
        unsafe {
            val bytes = PlotlyHtmlFragment::class.java.getResourceAsStream("/js/plotlyConnect.js")!!.readAllBytes()
            +bytes.toString(Charsets.UTF_8)
        }
    }
}

/**
 * Global plotly jupyter configuration
 */
@UnstablePlotlyAPI
public val Plotly.jupyter: PlotlyServerConfiguration
    get() = PlotlyServerConfiguration

@JupyterLibrary
internal class PlotlyServerIntegration : JupyterIntegration() {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var server: ApplicationEngine? = null

    private val plots = HashMap<String, Plot>()

    private var renderer: PlotlyRenderer = StaticPlotlyRenderer

    private fun start(config: PlotlyServerConfiguration): PlotlyHtmlFragment = if (server != null) {
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
        config.meta.onChange(this) { name ->
            if (name.toString() != PlotlyServerConfiguration::legacyMode.name ) {
                logger.info("Plotly server config parameter $name changed")
                doStart()
            }
        }
        doStart()
    }

    private fun stop() {
        server?.stop(1000, 2000)
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

    private fun renderPage(page: PlotlyPage): String = page.copy(renderer = renderer).render()

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

        import("space.kscience.plotly.server.jupyter")

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> { plot ->
            if (PlotlyServerConfiguration.legacyMode) {
                HTML(
                    Plotly.page(cdnPlotlyHeader, plotlyConnectHeader, renderer = renderer) {
                        plot(renderer = renderer, plot = plot)
                    }.render(), true
                )
            } else {
                HTML(renderPlot(plot))
            }
        }

        render<PlotlyFragment> { fragment ->
            if (PlotlyServerConfiguration.legacyMode) {
                HTML(
                    Plotly.page(cdnPlotlyHeader, plotlyConnectHeader, renderer = renderer) { renderer ->
                        fragment.render(this, renderer)
                    }.render(), true
                )
            } else {
                HTML(renderFragment(fragment))
            }
        }

        render<PlotlyPage> { page ->
            HTML(page.copy(headers = page.headers + plotlyConnectHeader, renderer = renderer).render(), true)
        }

        onLoaded {
            val config = PlotlyServerConfiguration
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
