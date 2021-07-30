package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.toName
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.asValue
import space.kscience.plotly.doubleInRange


public enum class XPeriodAlignment{
    start,
    middle,
    end
}

public class CandleStickLine: Scheme(){
    public val fillcolor: Color by color()
    public val lineColor: Color by color("line.color".toName())
    public var lineWidth: Double by double(2.0, key = "line.width".toName())
    public companion object: SchemeSpec<CandleStickLine>(::CandleStickLine)
}

public class CandleStick : Trace() {
    init {
        type = TraceType.candlestick
    }

    /**
     * Assigns id labels to each datum. These ids for object constancy of data points during animation.
     * Should be an array of strings, not numbers or any other type.
     */
    public var ids: List<String>? by stringList()

    /**
     * Only relevant when the axis `type` is "date". Sets the period positioning in milliseconds or "M<n>" on the x axis.
     * Special values in the form of "M<n>" could be used to declare the number of months. In this case `n` must be a positive integer.
     * Default 0
     */
    public var xperiod: Value by value { 0.asValue() }

    public var xperiod0: Value? by value()

    /**
     * Only relevant when the axis `type` is "date". Sets the alignment of data points on the x axis.
     */
    public var xperiodalignment: XPeriodAlignment by enum(XPeriodAlignment.middle)

    public val open: TraceValues by axis
    public val high: TraceValues by axis
    public val close: TraceValues by axis
    public val low: TraceValues by axis
    public val hovertext: TraceValues by axis


    public var meta: Value? by value()

    /**
     * Sets a reference between this trace's x coordinates and a 2D cartesian x axis. If "x" (the default value),
     * the x coordinates refer to `layout.xaxis`. If "x2", the x coordinates refer to `layout.xaxis2`, and so on.
     * Default "x"
     */
    public var xaxis: String by string("x")

    /**
     * Sets a reference between this trace's y coordinates and a 2D cartesian y axis. If "y" (the default value),
     * the y coordinates refer to `layout.yaxis`. If "y2", the y coordinates refer to `layout.yaxis2`, and so on.
     */
    public var yaxis: String by string("y")


    public var lineWidth: Double by double(2.0, "line.width".toName())

    public var increasing: CandleStickLine by spec(CandleStickLine)
    public var decreasing: CandleStickLine by spec(CandleStickLine)

    /**
     * Number between 0 and 1.
     *
     * Selects the width of the whiskers relative to the box´s width.
     * For example, with 1, the whiskers are as wide as the box(es).
     * Default 0
     */
    public var whiskerwidth: Double by doubleInRange(0.0..1.0)

    public companion object: SchemeSpec<CandleStick>(::CandleStick)
}