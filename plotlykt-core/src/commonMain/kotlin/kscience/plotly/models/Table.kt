package kscience.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.spec
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import kscience.plotly.numberGreaterThan

/**
 * Scheme to define table cell colors.
 * */
public class Fill : Scheme() {

    /**
     * Sets the cell fill color. It accepts a specific color.
     * */
    public var color: Color = Color(this, "color".asName())

    /**
     * Sets the cell fill color. It accepts an array of colors.
     * */
    public fun colors(array: Iterable<Any>) {
        color.value = array.map { Value.of(it) }.asValue()
    }

    public companion object : SchemeSpec<Fill>(::Fill)
}

public class Header : Scheme() {

    /**
     * Cell values. `values[m][n]` represents the value of the "n"-th point in column "m", therefore
     * the `values[m]` vector length for all columns must be the same (longer vectors will be truncated).
     *
     * Each value must be a finite number or a string.
     * */
    public var values: TraceValues = TraceValues(this, "values".asName())

    /**
     * The height of cells.
     * */
    public var height: Number by numberGreaterThan(1)

    /**
     * Sets the horizontal alignment of the `text` within the box. Has an effect only if `text` spans
     * two or more lines (i.e. `text` contains one or more <br> HTML tags) or if an explicit width is set
     * to override the text width.
     *
     * Defaults to `center`.
     * */
    public var align: TraceValues = TraceValues(this, "align".asName())

    /**
     * [LayoutLine] type object.
     * */
    public var line: LayoutLine by spec(LayoutLine)

    /**
     * [Fill] type object.
     * */
    public var fill: Fill by spec(Fill)

    /**
     * [Font] type object.
     * */
    public var font: Font by spec(Font)

    public fun values(array: Iterable<Any>) {
        values.set(array)
    }

    public fun align(align: HorizontalAlign) {
        align(listOf(align))
    }

    public fun align(alignments: List<HorizontalAlign>) {
        this.align.set(alignments)
    }

    public fun align(vararg alignments: HorizontalAlign) {
        this.align.set(alignments.toList())
    }

    public fun fill(block: Fill.() -> Unit) {
        fill = Fill(block)
    }

    public fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    public companion object : SchemeSpec<Header>(::Header)
}

public class Cells : Scheme() {

    /**
     * Cell values. `values[m][n]` represents the value of the "n"-th point in column "m", therefore
     * the `values[m]` vector length for all columns must be the same (longer vectors will be truncated).
     *
     * Each value must be a finite number or a string.
     * */
    public var values: TraceValues = TraceValues(this, "values".asName())

    /**
     * The height of cells.
     * */
    public var height: Number by numberGreaterThan(1)

    /**
     * Sets the horizontal alignment of the `text` within the box. Has an effect only if `text` spans
     * two or more lines (i.e. `text` contains one or more <br> HTML tags) or if an explicit width is set
     * to override the text width.
     *
     * Defaults to `center`.
     * */
    public var align: TraceValues = TraceValues(this, "align".asName())

    /**
     * [LayoutLine] type object.
     * */
    public var line: LayoutLine by spec(LayoutLine)

    /**
     * [Fill] type object.
     * */
    public var fill: Fill by spec(Fill)

    /**
     * [Font] type object.
     * */
    public var font: Font by spec(Font)

    public fun values(array: Iterable<Any>) {
        values.set(array)
    }

    public fun align(align: HorizontalAlign) {
        align(listOf(align))
    }

    public fun align(alignments: List<HorizontalAlign>) {
        this.align.set(alignments)
    }

    public fun align(vararg alignments: HorizontalAlign) {
        this.align.set(alignments.toList())
    }

    public fun fill(block: Fill.() -> Unit) {
        fill = Fill(block)
    }

    public fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    public fun line(block: LayoutLine.() -> Unit) {
        line = LayoutLine(block)
    }

    public companion object : SchemeSpec<Cells>(::Cells)
}

/**
 * Table view for detailed data viewing. The data are arranged in a grid of rows and columns. Most styling can
 * be specified for columns, rows or individual cells. Table is using a column-major order, ie. the
 * grid is represented as a vector of column vectors.
 *
 * For docs, see: [Plotly JS Table Reference](https://plotly.com/javascript/reference/table/#table)
 * */
public class Table : Trace() {
    init {
        type = TraceType.table
    }

    /**
     * Assigns id labels to each datum. These ids for object constancy of data points during animation.
     *
     * Should be an array of strings, not numbers or any other type.
     * */
    public var ids: TraceValues = TraceValues(this, "ids".asName())

    /**
     * Specifies the rendered order of the data columns; for example, a value `2` at position `0`
     * means that column index `0` in the data will be rendered as the third column,
     * as columns have an index base of zero.
     * */
    public var columnorder: TraceValues = TraceValues(this, "columnorder".asName())


    /**
     * The width of columns expressed as a ratio. Columns fill the available width
     * in proportion of their specified column widths.
     * */
    public var columnwidth: TraceValues = TraceValues(this, "columnwidth".asName())

    /**
     * [Header] type object. Used to define the header row.
     * */
    public var header: Header by spec(Header)

    /**
     * [Cells] type object. Used to define rows containing data.
     * */
    public var cells: Cells by spec(Cells)

    public fun header(block: Header.() -> Unit) {
        header = Header(block)
    }

    public fun cells(block: Cells.() -> Unit) {
        cells = Cells(block)
    }

    public companion object : SchemeSpec<Table>(::Table)
}