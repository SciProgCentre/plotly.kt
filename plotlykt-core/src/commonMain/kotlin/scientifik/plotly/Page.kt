package  scientifik.plotly

import hep.dataforge.meta.MetaItem
import hep.dataforge.names.Name
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id

interface Page : Iterable<Plot2D> {
    val title: String?
    operator fun get(plotId: String): Plot2D?
}

interface PageListener {
    fun traceChanged(plotId: String, traceId: Int, itemName: Name, item: MetaItem<*>?)
    fun layoutChanged(plotId: String, itemName: Name, item: MetaItem<*>?)
}

class PlotCell(val plot: Plot2D, val plotId: String, val rowNumber: Int, val size: Int, val colOrderNumber: Int) {
    init {
        if (size !in 0..12) throw IllegalArgumentException("Size $size should be in range 0..12")
    }
}

class PlotGrid(private val listener: PageListener? = null) : Page {

    override var title: String? = null

    private val _cells: MutableList<PlotCell> = ArrayList()
    val cells: List<PlotCell> get() = _cells

    override fun iterator(): Iterator<Plot2D> = _cells.map { it.plot }.iterator()

    override fun get(plotId: String): Plot2D? = _cells.find { it.plotId == plotId }?.plot

    fun plot(
        plot: Plot2D,
        rowNumber: Int = Int.MAX_VALUE,
        size: Int = 0,
        colOrderNumber: Int = Int.MAX_VALUE,
        id: String = plot.toString()
    ): Plot2D {
        _cells.add(PlotCell(plot, id, rowNumber, size, colOrderNumber))
        //Add listeners
        listener?.let { listener ->
            plot.layout.config.onChange(this) { itemName, _, item ->
                listener.layoutChanged(
                    id,
                    itemName,
                    item
                )
            }
            plot.data.forEachIndexed { index, trace ->
                trace.config.onChange(this) { itemName, _, item ->
                    listener.traceChanged(
                        id,
                        index,
                        itemName,
                        item
                    )
                }
            }
        }
        return plot
    }

    fun plot(
        rowNumber: Int = Int.MAX_VALUE,
        size: Int = 0,
        colOrderNumber: Int = Int.MAX_VALUE,
        id: String? = null,
        block: Plot2D.() -> Unit
    ): Plot2D {
        val plot = Plotly.plot2D(block)
        return plot(plot, rowNumber, size, colOrderNumber, id ?: plot.toString())
    }
}

fun FlowContent.plotRow(row: Pair<Int, List<PlotCell>>) = div("row") {
    row.second.mapIndexed { idx, cell ->
        if (cell.size != 0) {
            div("col col-${cell.size}") {
                id = "${row.first}-$idx"
            }
        } else {
            div("col") {
                id = "${row.first}-$idx"
            }
        }
    }
}

fun FlowContent.plotGrid(rows: List<Pair<Int, List<PlotCell>>>) = div("container") {
    rows.forEach {
        plotRow((it))
    }
}
