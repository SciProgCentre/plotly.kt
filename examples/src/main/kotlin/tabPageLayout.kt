import kotlinx.html.*
import space.kscience.plotly.*
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke
import space.kscience.plotly.palettes.T10
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


public class PlotTabs {
    public data class Tab(val title: String, val id: String, val content: PlotlyFragment)

    private val _tabs = ArrayList<Tab>()
    public val tabs: List<Tab> get() = _tabs

    public fun tab(title: String, id: String = title, block: FlowContent.(renderer: PlotlyRenderer) -> Unit) {
        _tabs.add(Tab(title, id, PlotlyFragment(block)))
    }
}

public fun Plotly.tabs(tabsID: String = "tabs", block: PlotTabs.() -> Unit): PlotlyPage {
    val grid = PlotTabs().apply(block)

    return page(cdnBootstrap, cdnPlotlyHeader) { container ->
        ul("nav nav-tabs") {
            role = "tablist"
            id = tabsID
            grid.tabs.forEachIndexed { index, tab ->
                li("nav-item") {
                    a(classes = "nav-link") {
                        if (index == 0) {
                            classes = classes + "active"
                        }
                        id = "${tab.id}-tab"
                        attributes["data-toggle"] = "tab"
                        href = "#${tab.id}"
                        role = "tab"
                        attributes["aria-controls"] = tab.id
                        attributes["aria-selected"] = "true"
                        +tab.title
                    }
                }
            }
        }
        div("tab-content") {
            id = "$tabsID-content"
            grid.tabs.forEachIndexed { index, tab ->
                div("tab-pane fade") {
                    if (index == 0) {
                        classes = classes + setOf("show", "active")
                    }
                    id = tab.id
                    role = "tabpanel"
                    attributes["aria-labelledby"] = "${tab.id}-tab"
                    tab.content.render(this, container)
                }
            }
        }
    }
}


@Suppress("DEPRECATION")
@UnstablePlotlyAPI
fun main() {

    val x = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x.map { sin(2.0 * PI * it) }
    val y2 = x.map { cos(2.0 * PI * it) }

    val trace1 = Trace(x, y1) {
        name = "sin"
        marker.color(T10.BLUE)

    }

    val trace2 = Trace(x, y2) {
        name = "cos"
        marker.color(T10.ORANGE)

    }

    val responsive = PlotlyConfig {
        responsive = true
    }

    val plot = Plotly.tabs {

        tab("First") {
            plot(config = responsive) {
                traces(trace1)
                layout {
                    title = "First graph"
                    xaxis.title = "x axis name"
                    xaxis.title = "y axis name"
                }
            }
        }
        tab("Second") {
            plot(config = responsive) {
                traces(trace2)
                layout {
                    title = "Second graph"
                    xaxis.title = "x axis name"
                    xaxis.title = "y axis name"
                }
            }
        }
    }

    plot.makeFile()
}
