package  scientifik.plotly

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

@UnstablePlotlyAPI
class PlotGrid : PlotlyPage {
    data class PlotCell(val id: String, val plot: Plot2D, val row: Int, val col: Int, val width: Int = 1)

    private val cells = HashMap<String, PlotCell>()

    val grid
        get() = cells.values.groupBy { it.row }.values.map {
            it.sortedBy { plot -> plot.col }
        }.toList()

    operator fun get(id: String): PlotCell? = cells[id]

    private var currentRow = 0
    private var currentCol = 0

    fun plot(
        plot: Plot2D,
        id: String = plot.toString(),
        width: Int = 6,
        row: Int? = null,
        col: Int? = null
    ): Plot2D {
        val actualRow = if (row != null) {
            row
        } else {
            currentCol = 0
            currentRow++
        }

        val actualColumn = col ?: currentCol++

        cells[id] = PlotCell(id, plot, actualRow, actualColumn, width)

        return plot
    }

    fun plot(
        row: Int? = null,
        col: Int? = null,
        id: String? = null,
        width: Int = 6,
        block: Plot2D.() -> Unit
    ): Plot2D {
        val plot = Plotly.plot2D(block)
        return plot(plot, id ?: plot.toString(), width, row, col)
    }

    override fun FlowContent.renderPage(container: PlotlyContainer) {
        div {
            style = "display: flex; flex-direction: column;"
            grid.forEach { row ->
                div {
                    style = "display: flex; flex-direction: row;"
                    row.forEach { cell ->
                        div {
                            style = "flex-grow: ${cell.width};"
                            plot(cell.plot, cell.id, container = container)
                        }
                    }
                }
            }
        }
    }
}

@UnstablePlotlyAPI
fun Plotly.grid(block: PlotGrid.() -> Unit): PlotGrid = PlotGrid().apply(block)
