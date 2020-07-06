package  scientifik.plotly

import hep.dataforge.meta.Config
import hep.dataforge.meta.Configurable
import hep.dataforge.meta.MetaItem
import hep.dataforge.meta.string
import hep.dataforge.names.Name

@UnstablePlotlyAPI
interface PageListener {
    fun traceChanged(plotId: String, traceId: Int, itemName: Name, item: MetaItem<*>?)
    fun layoutChanged(plotId: String, itemName: Name, item: MetaItem<*>?)
}

@UnstablePlotlyAPI
class PlotGrid(private val listener: PageListener? = null) : Configurable{
    override var config: Config = Config()

    var title by string()

    data class PlotCell(val id: String, val plot: Plot2D, val row: Int, val col: Int, val width: Int = 1)

    private val cells = HashMap<String, PlotCell>()

    val grid get() = cells.values.groupBy { it.row }.values.map {
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
        //Add listeners
        listener?.let { listener ->
            plot.layout.config.onChange(this) { itemName, _, item ->
                listener.layoutChanged(id, itemName, item)
            }
            plot.data.forEachIndexed { index, trace ->
                trace.config.onChange(this) { itemName, _, item ->
                    listener.traceChanged(id, index, itemName, item)
                }
            }
        }
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

}