package scientifik.plotly

import kotlinx.html.*

fun FlowContent.plot(plot: Plot2D, plotId: String = plot.toString()):Plot2D {
    div {
        id = plotId
        script {
            val tracesString = plot.data.toJsonString()
            val layoutString = plot.layout.toJsonString()
            unsafe {
                //language=JavaScript
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
    return plot
}

fun FlowContent.plot(plotId: String? = null, builder: Plot2D.() -> Unit): Plot2D {
    val plot = Plot2D().apply(builder)
    return plot(plot, plotId ?: plot.toString())
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