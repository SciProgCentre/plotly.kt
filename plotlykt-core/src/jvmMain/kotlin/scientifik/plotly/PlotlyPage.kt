package scientifik.plotly

import kotlinx.html.FlowContent

interface PlotlyContainer {
    fun FlowContent.renderPlot(
        plot: Plot2D,
        plotId: String = plot.toString(),
        config: PlotlyConfig = PlotlyConfig()
    ): Plot2D
}

class StaticPlotlyContainer(val parent: FlowContent) : PlotlyContainer {
    override fun FlowContent.renderPlot(plot: Plot2D, plotId: String, config: PlotlyConfig): Plot2D =
        staticPlot(plot, plotId, config)
}

fun FlowContent.plot(
    plot: Plot2D,
    plotId: String = plot.toString(),
    config: PlotlyConfig = PlotlyConfig(),
    container: PlotlyContainer = StaticPlotlyContainer(this)
): Plot2D = with(container) {
    renderPlot(plot, plotId, config)
}

fun FlowContent.plot(
    plotId: String? = null,
    plotlyConfig: PlotlyConfig = PlotlyConfig(),
    handle: PlotlyContainer = StaticPlotlyContainer(this),
    builder: Plot2D.() -> Unit
): Plot2D {
    val plot = Plot2D().apply(builder)
    return plot(plot, plotId ?: plot.toString(), plotlyConfig, handle)
}

class PlotlyPage(val renderPage: FlowContent.(container: PlotlyContainer) -> Unit)

fun Plotly.page(renderPage: FlowContent.(container: PlotlyContainer) -> Unit) = PlotlyPage(renderPage)

///**
// * Create a custom layout with loaded plotly dependency
// */
//fun Plotly.page(
//    vararg headers: HtmlHeader,
//    title: String? = null,
//    page: PlotlyPage
//): String {
//    return createHTML().html {
//        head {
//            meta {
//                charset = "utf-8"
//            }
//            applyHeaders(headers)
//            title(title ?: "Plotly.kt")
//        }
//        body {
//            with(page) {
//                renderPage(StaticPlotlyContainer(this@body))
//            }
//        }
//    }
//}
//
//fun Plotly.page(
//    vararg headers: HtmlHeader,
//    title: String? = null,
//    pageBuilder: FlowContent.(container: PlotlyContainer) -> Unit
//): String = page(headers = *headers, title = title, page = PlotlyPage(pageBuilder))
