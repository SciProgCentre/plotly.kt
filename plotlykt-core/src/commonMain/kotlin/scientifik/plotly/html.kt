package scientifik.plotly

import kotlinx.html.*

fun FlowContent.plot(plotId: String, plot: Plot2D, divConfig: DIV.() -> Unit = {}) {
    div {
        id = plotId
        divConfig()
    }
    script {
        val tracesString = plot.data.toJsonString()
        val layoutString = plot.layout.toJsonString()
        unsafe {
            +"""
            Plotly.newPlot(
                '$plotId',
                 $tracesString,
                 $layoutString,
                 {showSendToCloud: true}
            );
            """.trimIndent()
        }
    }
}

@UnstablePlotlyAPI
fun FlowContent.plotGrid(plotGrid: PlotGrid) {
    div {
        style = "display: flex; flex-direction: column;"
        plotGrid.grid.forEach { row ->
            div {
                style = "display: flex; flex-direction: row;"
                row.forEach { cell ->
                    plot(cell.id, cell.plot) {
                        style = "flex-grow: ${cell.width};"
                    }
                }
            }
        }
    }
}