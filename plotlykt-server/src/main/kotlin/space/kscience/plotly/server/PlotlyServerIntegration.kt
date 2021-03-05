package space.kscience.plotly.server

import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.*
import space.kscience.plotly.*

@JupyterLibrary
internal class PlotlyServerIntegration : JupyterIntegration() {

    private val jsBundle = ResourceFallbacksBundle(listOf(
        org.jetbrains.kotlinx.jupyter.api.libraries.ResourceLocation(
            "https://cdnjs.cloudflare.com/ajax/libs/plotly.js/1.54.6/plotly.min.js",
            ResourcePathType.URL
        ),
        org.jetbrains.kotlinx.jupyter.api.libraries.ResourceLocation(
            "js/plotly.min.js",
            ResourcePathType.CLASSPATH_PATH
        )
    ))

    private val jsResource = LibraryResource(name = "Plotly", type = ResourceType.JS, bundles = listOf(jsBundle))

    val port = System.getProperty("kscience.plotly.port")?.toInt() ?: 8882

    @UnstablePlotlyAPI
    override fun Builder.onLoaded() {

        resource(jsResource)

        import(
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.plotly.server.JupyterPlotlyServer",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*"
        )

        render<HtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> {
            HTML(JupyterPlotlyServer.renderPlot(it))
        }

        render<PlotlyFragment> {
            HTML(JupyterPlotlyServer.renderFragment(it))
        }

        render<PlotlyPage> {
            HTML(JupyterPlotlyServer.renderPage(it), true)
        }

        onLoaded {
            display(HTML(JupyterPlotlyServer.start(port).toString()))
        }

        onShutdown {
            display(HTML(JupyterPlotlyServer.stop().toString()))
        }
    }

}
