package scientifik.plotly

import kotlinx.html.*

fun FlowContent.plot(plot: Plot2D, plotId: String = plot.toString()) {
    div {
        id = plotId
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

fun FlowContent.plot(plotId: String? = null, builder: Plot2D.() -> Unit) {
    val plot = Plot2D().apply(builder)
    plot(plot, plotId ?: plot.toString())
}

@UnstablePlotlyAPI
fun FlowContent.plotGrid(plotGrid: PlotGrid) {
    div {
        style = "display: flex; flex-direction: column;"
        plotGrid.grid.forEach { row ->
            div {
                style = "display: flex; flex-direction: row;"
                row.forEach { cell ->
                    div {
                        style = "flex-grow: ${cell.width};"
                        plot(cell.plot, cell.id)
                    }
                }
            }
        }
    }
}