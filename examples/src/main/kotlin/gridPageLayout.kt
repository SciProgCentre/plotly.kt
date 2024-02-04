import kotlinx.html.div
import space.kscience.plotly.*
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke
import space.kscience.plotly.palettes.T10
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


private class PlotGrid {
    public data class PlotCell(val id: String, val plot: Plot, val row: Int, val col: Int, val width: Int = 1)

    private val cells = HashMap<String, PlotCell>()

    /**
     * @return Columns in ascending order, grouped by rows in ascending order.
     * */
    public val grid: List<List<PlotCell>>
        get() = cells.values.groupBy { it.row }.toSortedMap().values.map {
            it.sortedBy { cell -> cell.col }
        }.toList()

    public operator fun get(id: String): PlotCell? = cells[id]

    private var currentRow = 0
    private var currentCol = 0

    public fun plot(
        plot: Plot,
        id: String = plot.toString(),
        width: Int = 6,
        row: Int? = null,
        col: Int? = null,
    ): Plot {
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

    public fun plot(
        row: Int? = null,
        col: Int? = null,
        id: String? = null,
        width: Int = 6,
        block: Plot.() -> Unit,
    ): Plot {
        val plot = Plotly.plot(block)
        return plot(plot, id ?: plot.toString(), width, row, col)
    }
}


private fun Plotly.grid(block: PlotGrid.() -> Unit): PlotlyPage {
    val grid = PlotGrid().apply(block)
    return page(cdnBootstrap, cdnPlotlyHeader) { container ->
        div("col") {
            grid.grid.forEach { row ->
                div("row") {
                    row.forEach { cell ->
                        div("col-${cell.width}") {
                            plot(cell.plot, cell.id, renderer = container)
                        }
                    }
                }
            }
        }
    }
}

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

    val plot = Plotly.grid {
//        title = "Page sample"
        plot(row = 1, width = 8) {
            traces(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis.title = "x axis name"
                xaxis.title = "y axis name"
            }
        }

        plot(row = 1, width = 4) {
            traces(trace1, trace2)
            layout {
                title = "Second graph, row: 1, size: 4/12"
                xaxis.title = "x axis name"
                xaxis.title = "y axis name"
            }
        }

        plot(row = 2, width = 12) {
            traces(trace1, trace2)
            layout {
                title = "Third graph, row: 2, size: 12/12"
                xaxis.title = "x axis name"
                xaxis.title = "y axis name"
            }
        }
    }

    plot.makeFile()
}
