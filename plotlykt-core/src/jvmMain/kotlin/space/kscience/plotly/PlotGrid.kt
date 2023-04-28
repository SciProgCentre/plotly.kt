package  space.kscience.plotly

import kotlinx.html.div

@UnstablePlotlyAPI
@Deprecated("Discontinued in favor of VisionForge layouts")
public class PlotGrid {
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

@UnstablePlotlyAPI
@Deprecated("Discontinued in favor of VisionForge layouts")
public fun Plotly.grid(block: PlotGrid.() -> Unit): PlotlyPage {
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
