package kscience.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import kscience.plotly.UnsupportedPlotlyAPI
import kscience.plotly.lazySpec
import kscience.plotly.numberGreaterThan


public class Fill : Scheme() {

    public var color: Color = Color(this, "color".asName())

    public fun colors(array: Iterable<Any>) {
        color.value = array.map { Value.of(it) }.asValue()
    }

    public companion object : SchemeSpec<Fill>(::Fill)
}

public class Header : Scheme() {

    public var values: TraceValues = TraceValues(this, "values".asName())

    public var height: Number by numberGreaterThan(1)

    public var align: TraceValues = TraceValues(this, "align".asName())

    public var line: LayoutLine? by spec(LayoutLine)

    public var fill: Fill? by spec(Fill)

    public var font: Font? by spec(Font)

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

    public var values: TraceValues = TraceValues(this, "values".asName())

    public var height: Number by numberGreaterThan(1)

    public var align: TraceValues = TraceValues(this, "align".asName())

    public var line: LayoutLine? by spec(LayoutLine)

    public var fill: Fill? by spec(Fill)

    public var font: Font? by spec(Font)

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

@UnsupportedPlotlyAPI
public class Table : Trace() {
    init {
        type = TraceType.table
    }

    public var ids: TraceValues = TraceValues(this, "ids".asName())

    public var columnorder: TraceValues = TraceValues(this, "columnorder".asName())

    public var columnwidth: TraceValues = TraceValues(this, "columnwidth".asName())

    public var header: Header by lazySpec(Header)

    public var cells: Cells by lazySpec(Cells)

    public fun header(block: Header.() -> Unit) {
        header = Header(block)
    }

    public fun cells(block: Cells.() -> Unit) {
        cells = Cells(block)
    }

    public companion object : SchemeSpec<Table>(::Table)
}