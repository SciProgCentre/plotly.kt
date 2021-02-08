package kscience.plotly

import kotlinx.html.*

public class PlotTabs {
    public data class Tab(val title: String, val id: String, val content: PlotlyFragment)

    private val _tabs = ArrayList<Tab>()
    public val tabs: List<Tab> get() = _tabs

    public fun tab(title: String, id: String = title, block: FlowContent.(renderer: PlotlyRenderer) -> Unit) {
        _tabs.add(Tab(title, id, PlotlyFragment(block)))
    }
}

@UnstablePlotlyAPI
public fun Plotly.tabs(tabsID: String = "tabs", block: PlotTabs.() -> Unit): PlotlyPage {
    val grid = PlotTabs().apply(block)

    return page(cdnBootstrap, cdnPlotlyHeader) { container ->
        ul("nav nav-tabs") {
            role = "tablist"
            id = tabsID
            grid.tabs.forEachIndexed { index, tab->
                li("nav-item") {
                    a(classes = "nav-link") {
                        if(index == 0){
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
            grid.tabs.forEachIndexed {index, tab->
                div("tab-pane fade") {
                    if(index == 0){
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