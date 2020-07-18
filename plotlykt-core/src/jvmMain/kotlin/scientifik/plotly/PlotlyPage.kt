package scientifik.plotly

import kotlinx.html.FlowContent

//TODO add default config and headers
interface PlotlyPage {
    //fun defaultHeaders(location: ResourceLocation): Collection<HtmlVisitor> = listOf(PlotlyCdnHeader)
    fun FlowContent.renderPage(container: PlotlyContainer)
}

fun Plotly.page(content: FlowContent.(container: PlotlyContainer) -> Unit) = object : PlotlyPage {
    override fun FlowContent.renderPage(container: PlotlyContainer) {
        content(container)
    }
}