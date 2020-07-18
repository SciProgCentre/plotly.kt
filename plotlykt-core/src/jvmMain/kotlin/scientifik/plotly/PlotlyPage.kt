package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML

class PlotlyFragment(val render: FlowContent.(container: PlotlyContainer) -> Unit)

fun PlotlyFragment.toPage(
    vararg headers: HtmlFragment = arrayOf(cdnPlotlyHeader),
    title: String = "Plotly.kt",
    container: PlotlyContainer = StaticPlotlyContainer
) = PlotlyPage(this, headers.toList(), title, container)

data class PlotlyPage(
    val fragment: PlotlyFragment,
    val headers: Collection<HtmlFragment>,
    val title: String = "Plotly.kt",
    val container: PlotlyContainer = StaticPlotlyContainer
) {
    fun render(): String = createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            title(this@PlotlyPage.title)
            headers.distinct().forEach { it.visit(consumer) }
        }
        body {
            fragment.render(this, container)
        }
    }
}


fun Plotly.fragment(content: FlowContent.(container: PlotlyContainer) -> Unit): PlotlyFragment = PlotlyFragment(content)

fun Plotly.page(
    vararg headers: HtmlFragment = arrayOf(cdnPlotlyHeader),
    title: String = "Plotly.kt",
    container: PlotlyContainer = StaticPlotlyContainer,
    content: FlowContent.(container: PlotlyContainer) -> Unit
) = PlotlyPage(fragment(content), headers.toList(), title, container)