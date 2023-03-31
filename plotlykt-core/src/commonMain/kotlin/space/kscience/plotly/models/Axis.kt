package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.plotly.listOfValues
import space.kscience.plotly.numberGreaterThan
import space.kscience.plotly.numberInRange
import kotlin.js.JsName


public enum class AxisType {
    @JsName("default")
    `-`,
    linear,
    log,
    date,
    category,
    multicategory;
}

public enum class TickMode {
    array,
    auto,
    linear
}

public enum class Ticks {
    @Suppress("EnumEntryName", "DANGEROUS_CHARACTERS")
    @JsName("empty")
    `""`,
    inside,
    outside
}

public enum class AxisSide {
    top, bottom, left, right
}

/**
 * Axis scheme
 */
public class Axis : Scheme() {
    /**
     * Sets the title of this axis.
     */
    public var title: String?
        get() = meta["title.text"].string ?: meta["title"].string
        set(value) {
            meta["title"] = value?.asValue()
        }

    /**
     * Enumerated, one of ( "-" | "linear" | "log" | "date" | "category" | "multicategory" ) .
     * Sets the axis type. By default, plotly attempts to determine
     * the axis type by looking into the data of the traces
     * that referenced the axis in question.
     * Default: "-"
     */
    public var type: AxisType by enum(AxisType.`-`)

    /**
     * A single toggle to hide the axis while preserving interaction
     * like dragging. Default is true when a cheater plot
     * is present on the axis, otherwise false
     */
    public var visible: Boolean? by boolean()

    /**
     * Sets the tick length (in px).
     * Default: 5.
     */
    public var ticklen: Number by numberGreaterThan(0)

    /**
     * Sets the tick width (in px).
     * Default: 1.
     */
    public var tickwidth: Number by numberGreaterThan(0)

    /**
     * Sets the tick color.
     * Default: #444.
     */
    public val tickcolor: Color by color()

    /**
     * Sets the angle of the tick labels with respect to the horizontal.
     * For example, a `tickangle` of -90 draws the tick labels vertically.
     */
    public var tickangle: Number by numberInRange(-360.0..360.0)

    /**
     * Sets the tick font.
     */
    public var tickfont: Font by spec(Font)

    /**
     * Determines whether ticks are drawn or not. If "", this axis' ticks are not drawn.
     * If "outside" ("inside"), this axis' are drawn outside (inside) the axis lines.
     */
    public var ticks: Ticks by enum(Ticks.inside)

    /**
     * Sets the step in-between ticks on this axis. Use with `tick0`. Must be a positive number, or special strings
     * available to "log" and "date" axes. If the axis `type` is "log", then ticks are set every 10^(n"dtick)
     * where n is the tick number. For example, to set a tick mark at 1, 10, 100, 1000, ... set dtick to 1.
     * To set tick marks at 1, 100, 10000, ... set dtick to 2. To set tick marks at 1, 5, 25, 125, 625, 3125, ...
     * set dtick to log_10(5), or 0.69897000433. "log" has several special values; "L<f>", where `f`
     * is a positive number, gives ticks linearly spaced in value (but not position). For example `tick0` = 0.1,
     * `dtick` = "L0.5" will put ticks at 0.1, 0.6, 1.1, 1.6 etc. To show powers of 10 plus small digits between,
     * use "D1" (all digits) or "D2" (only 2 and 5). `tick0` is ignored for "D1" and "D2". If the axis `type` is "date",
     * then you must convert the time to milliseconds. For example, to set the interval between ticks to one day,
     * set `dtick` to 86400000.0. "date" also has special values "M<n>" gives ticks spaced by a number of months.
     * `n` must be a positive integer. To set ticks on the 15th of every third month, set `tick0` to "2000-01-15"
     * and `dtick` to "M3". To set ticks every 4 years, set `dtick` to "M48"
     */
    public var dtick: Value? by value()

    /**
     * Determines whether or not the range of this axis is computed
     * in relation to the input data. See `rangemode` for more info.
     * If `range` is provided, then `autorange` is set to "false".
     * Default: true.
     */
    public var autorange: Boolean by boolean(true)

    /**
     * Sets the range of this axis. If the axis `type` is "log",
     * then you must take the log of your desired range
     * (e.g. to set the range from 1 to 100, set the range from 0 to 2).
     * If the axis `type` is "date", it should be date strings, like date data,
     * though Date objects and unix milliseconds will be accepted and
     * converted to strings. If the axis `type` is "category", it should be
     * numbers, using the scale where each category is assigned
     * a serial number from zero in the order it appears.
     */
    @Deprecated("Use range() instead")
    public var range: ClosedFloatingPointRange<Double>?
        get() = meta["range"]?.value?.doubleArray?.let { it[0]..it[1] }
        set(value) {
            meta["range"] = value?.let { ListValue(listOf(value.start.asValue(), value.endInclusive.asValue())) }
        }

    /**
     * Set the range using arbitrary values
     */
    public fun range(from: Value, to: Value) {
        meta["range"] = ListValue(listOf(from, to))
    }

    /**
     * Set range using double kotlin range
     */
    public fun range(value: ClosedFloatingPointRange<Double>) {
        range(value.start.asValue(), value.endInclusive.asValue())
    }

    /**
     * Set range using kotlin string range
     */
    public fun range(value: ClosedRange<String>) {
        range(value.start.asValue(), value.endInclusive.asValue())
    }


    /**
     * Sets default for all colors associated with this axis all at once:
     * line, font, tick, and grid colors. Grid color is lightened
     * by blending this with the plot background Individual pieces can override this.
     * Default: #444.
     */
    public val color: Color by color()

    /**
     * Determines whether or not a line bounding this axis is drawn.
     */
    public var showline: Boolean? by boolean()

    /**
     * Sets the axis line color. Default: #444.
     */
    public val linecolor: Color by color()

    /**
     * Sets the width (in px) of the axis line. Default: 1.
     */
    public var linewidth: Number by numberGreaterThan(0)

    /**
     * Determines whether or not grid lines are drawn. If "true",
     * the grid lines are drawn at every tick mark.
     */
    public var showgrid: Boolean? by boolean()

    /**
     * Sets the color of the grid lines. Default: #eee
     */
    public val gridcolor: Color by color()

    /**
     * Sets the width (in px) of the grid lines. Default: 1.
     */
    public var gridwidth: Number by numberGreaterThan(0)

    /**
     * Determines whether or not a line is drawn at along the 0 value of this axis.
     * If "true", the zero line is drawn on top of the grid lines.
     */
    public var zeroline: Boolean? by boolean()

    /**
     * Sets the line color of the zero line. Default: #444
     */
    public val zerolinecolor: Color by color()

    /**
     * Sets the width (in px) of the zero line. Default: 1.
     */
    public var zerolinewidth: Number by numberGreaterThan(0)

    /**
     * Sets the tick mode for this axis. If "auto", the number of ticks is set via `nticks`.
     * If "linear", the placement of the ticks is determined by a starting position `tick0`
     * and a tick step `dtick` ("linear" is the default value if `tick0` and `dtick` are provided).
     * If "array", the placement of the ticks is set via `tickvals` and the tick text is `ticktext`.
     * ("array" is the default value if `tickvals` is provided).
     */
    public var tickmode: TickMode by enum(TickMode.auto)

    /**
     * Sets the values at which ticks on this axis appear.
     * Only has an effect if `tickmode` is set to "array". Used with `ticktext`.
     */
    public var tickvals: List<Value> by listOfValues()

    /**
     *Sets the text displayed at the ticks position via `tickvals`.
     * Only has an effect if `tickmode` is set to "array". Used with `tickvals`.
     */
    public var ticktext: List<Value> by listOfValues()

    /**
     * Determines whether or not the tick labels are drawn.
     */
    public var showticklabels: Boolean? by boolean()

    public var autotick: Boolean? by boolean()

    /**
     * Determines whether long tick labels automatically grow the figure margins.
     */
    public var automargin: Boolean? by boolean()

    /**
     * Enumerated, one of ( "free" | "/^x([2-9]|[1-9][0-9]+)?$/" | "/^y([2-9]|[1-9][0-9]+)?$/" )
     * If set to an opposite-letter axis id (e.g. `x2`, `y`), this axis is bound to the corresponding
     * opposite-letter axis. If set to "free", this axis' position is determined by `position`.
     */
    public var anchor: String? by string()

    /**
     * Sets the position of this axis in the plotting space (in normalized coordinates).
     * Only has an effect if `anchor` is set to "free". Default: 0.
     */
    public var position: Number by numberInRange(0.0..1.0)

    /**
     * Determines whether a x (y) axis is positioned at the "bottom" ("left") or "top" ("right") of the plotting area.
     */
    public var side: AxisSide? by value<AxisSide?>(
        writer = { it?.name?.asValue() },
        reader = { value -> value?.string?.let { enumValueOf<AxisSide>(it) } }
    )

    /**
     * If set a same-letter axis id, this axis is overlaid on top of the corresponding same-letter axis,
     * with traces and axes visible for both axes. If "false", this axis does not overlay any same-letter axes.
     * In this case, for axes with overlapping domains only the highest-numbered axis will be visible.
     */
    public var overlaying: String? by string()

    public fun title(block: Title.() -> Unit) {
        val spec = Title.write(meta.getOrCreate("title"))
        spec.apply(block)
    }

    public fun tickfont(block: Font.() -> Unit) {
        tickfont = Font(block)
    }

    public fun tickvals(array: Iterable<Any>) {
        tickvals = array.map { Value.of(it) }
    }

    public fun ticktext(array: Iterable<Any>) {
        ticktext = array.map { Value.of(it) }
    }

    public companion object : SchemeSpec<Axis>(::Axis)
}