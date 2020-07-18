package scientifik.plotly

import kotlinx.html.FlowContent

//TODO add default config and headers
class PlotlyPage(val renderPage: FlowContent.(container: PlotlyContainer) -> Unit)

fun Plotly.page(content: FlowContent.(container: PlotlyContainer) -> Unit) = PlotlyPage(content)