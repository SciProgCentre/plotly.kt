@file:Suppress("EnumEntryName")

package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.plotly.list
import space.kscience.plotly.numberGreaterThan
import space.kscience.plotly.numberInRange
import kotlin.js.JsName


public enum class BarMode {
    stack,
    group,
    overlay,
    relative
}

public class Margin : Scheme() {
    /**
     * Turns on/off margin expansion computations. Legends, colorbars, updatemenus, sliders,
     * axis rangeselector and rangeslider are allowed to push the margins by defaults.
     */
    public var autoexpand: Boolean? by boolean()

    /**
     * Sets the amount of padding (in px) between the plotting area and the axis lines
     */
    public var pad: Number by numberGreaterThan(0)

    /**
     * Sets the left margin (in px). Default: 80.
     */
    public var l: Number by numberGreaterThan(0)

    /**
     * Sets the right margin (in px). Default: 80.
     */
    public var r: Number by numberGreaterThan(0)

    /**
     * Sets the top margin (in px). Default: 100.
     */
    public var t: Number by numberGreaterThan(0)

    /**
     * Sets the bottom margin (in px). Default: 80.
     */
    public var b: Number by numberGreaterThan(0)

    public companion object : SchemeSpec<Margin>(::Margin)
}

public enum class BoxMode {
    overlay,
    group
}

public enum class HoverMode {
    x,
    y,
    closest,
    `false`,

    @JsName("xUnified")
    `x unified`,

    @JsName("yUnified")
    `y unified`
}

public enum class BarNorm {
    fraction,
    percent,

    @Suppress("DANGEROUS_CHARACTERS")
    @JsName("empty")
    `""`
}

public enum class ViolinMode {
    group,
    overlay
}

public class Layout : Scheme() {
    /**
     * Sets the plot's width (in px).
     * Number greater than or equal to 10, default: 700.
     */
    public var width: Number by numberGreaterThan(10)

    /**
     * Sets the plot's height (in px).
     * Number greater than or equal to 10, default: 450.
     */
    public var height: Number by numberGreaterThan(10)

    /**
     * Determines whether or not a layout width or height that has been left undefined
     * by the user is initialized on each relayout. Note that, regardless of this attribute, an undefined layout
     * width or height is always initialized on the first call to plot.
     */
    public var autosize: Boolean? by boolean()

    /**
     * Sets the plot's title.
     */
    public var title: String?
        get() = this["title.text"].string ?: this["title"].string
        set(value) {
            this["title.text"] = value
        }

    public var xaxis: Axis by spec(Axis)

    public var yaxis: Axis by spec(Axis)

    /**
     * Enumerated, one of ( "stack" | "group" | "overlay" | "relative" )
     * Determines how bars at the same location coordinate
     * are displayed on the graph. With "stack", the bars
     * are stacked on top of one another With "relative",
     * the bars are stacked on top of one another, with negative values
     * below the axis, positive values above With "group", the bars
     * are plotted next to one another centered around the shared location.
     * With "overlay", the bars are plotted over one another, you might
     * need to an "opacity" to see multiple bars.
     * Default: "group".
     */
    public var barmode: BarMode by enum(BarMode.group)

    /**
     * Sets the normalization for bar traces on the graph. With "fraction", the value of each bar
     * is divided by the sum of all values at that location coordinate. "percent" is the same but multiplied
     * by 100 to show percentages. Default: "".
     */
    public var barnorm: BarNorm by enum(BarNorm.`""`)

    /**
     * Sets the gap (in plot fraction) between bars
     * of adjacent location coordinates.
     */
    public var bargap: Number by numberInRange(0.0..1.0)

    /**
     * Sets the gap (in plot fraction) between bars of the same location coordinate.
     * Default: 0.
     */
    public var bargroupgap: Number by numberInRange(0.0..1.0)

    /**
     * Determines how violins at the same location coordinate are displayed on the graph. If "group",
     * the violins are plotted next to one another centered around the shared location. If "overlay",
     * the violins are plotted over one another, you might need to set "opacity" to see them multiple violins.
     * Has no effect on traces that have "width" set.
     */
    public var violinmode: ViolinMode by enum(ViolinMode.overlay)

    /**
     * Sets the gap (in plot fraction) between violins of adjacent location coordinates.
     * Has no effect on traces that have "width" set. Default: 0.3
     */
    public var violingap: Number by numberInRange(0.0..1.0)

    /**
     * Sets the gap (in plot fraction) between violins of the same location coordinate.
     * Has no effect on traces that have "width" set. Default: 0.3
     */
    public var violingroupgap: Number by numberInRange(0.0..1.0)

    public var legend: Legend by spec(Legend)

    /**
     * An annotation is a text element that can be placed anywhere in the plot.
     * It can be positioned with respect to relative coordinates in the plot
     * or with respect to the actual data coordinates of the graph.
     * Annotations can be shown with or without an arrow.
     */
    public var annotations: List<Text> by list(Text)

    public var shapes: List<Shape> by list(Shape)

    /**
     * Sets the background color of the paper where the graph is drawn.
     * Default: #fff.
     */
    public val paper_bgcolor: Color by color()

    /**
     * Sets the background color of the plotting area in-between x and y axes.
     * Default: #fff.
     */
    public val plot_bgcolor: Color by color()

    public var margin: Margin by spec(Margin)

    /**
     * Determines how boxes at the same location coordinate are displayed on the graph. If "group",
     * the boxes are plotted next to one another centered around the shared location. If "overlay",
     * the boxes are plotted over one another, you might need to set "opacity" to see them multiple boxes.
     * Has no effect on traces that have "width" set.
     */
    public var boxmode: BoxMode by enum(BoxMode.overlay)

    /**
     * Sets the gap (in plot fraction) between boxes of adjacent location coordinates.
     * Has no effect on traces that have "width" set. Default: 0.3
     */
    public var boxgap: Number by numberInRange(0.0..1.0)

    /**
     * Sets the gap (in plot fraction) between boxes of the same location coordinate.
     * Has no effect on traces that have "width" set. Default: 0.3
     */
    public var boxgroupgap: Number by numberInRange(0.0..1.0)

    /**
     * Determines whether or not a legend is drawn. Default is `true` if there is a trace to show and any of these:
     * a) Two or more traces would by default be shown in the legend.
     * b) One pie trace is shown in the legend.
     * c) One trace is explicitly given with `showlegend: true`.
     */
    public var showlegend: Boolean? by boolean()

    /**
     * Determines the mode of hover interactions. If "closest", a single hoverlabel will appear for the "closest"
     * point within the `hoverdistance`. If "x" (or "y"), multiple hoverlabels will appear for multiple points
     * at the "closest" x- (or y-) coordinate within the `hoverdistance`, with the caveat that no more than one
     * hoverlabel will appear per trace. If "x unified" (or "y unified"), a single hoverlabel will appear
     * multiple points at the closest x- (or y-) coordinate within the `hoverdistance` with the caveat that
     * no more than one hoverlabel will appear per trace. In this mode, spikelines are enabled by
     * default perpendicular to the specified axis. If false, hover interactions are disabled. If `clickmode` includes
     * the "select" flag, `hovermode` defaults to "closest". If `clickmode` lacks the "select" flag, it defaults
     * to "x" or "y" (depending on the trace's `orientation` value) for plots based on cartesian coordinates.
     * For anything else the default value is "closest".
     */
    public var hovermode: HoverMode by enum(HoverMode.closest)

    /**
     * Sets the decimal and thousand separators. For example, ". " puts a '.' before decimals and a space
     * between thousands. In English locales, dflt is ".," but other locales may alter this default.
     */
    public var separators: String? by string()

    /**
     * Sets the default distance (in pixels) to look for data to add hover labels (-1 means no cutoff,
     * 0 means no looking for data). This is only a real distance for hovering on point-like objects, like
     * scatter points. For area-like objects (bars, scatter fills, etc) hovering is on inside the area and off outside,
     * but these objects will not supersede hover on point-like objects in case of conflict.
     * Default: 20.
     */
    public var hoverdistance: Number by numberGreaterThan(-1)

    /**
     * Sets the default calendar system to use for interpreting and displaying dates throughout the plot.
     */
    public var calendar: Calendar by enum(Calendar.gregorian)

    public fun legend(block: Legend.() -> Unit) {
        legend.apply(block)
    }

    public fun title(block: Title.() -> Unit) {
        Title.write(getChild("title")).apply(block)
    }

    //TODO moe title to parameter block
    public fun xaxis(block: Axis.() -> Unit) {
        xaxis.apply(block)
    }

    public fun yaxis(block: Axis.() -> Unit) {
        yaxis.apply(block)
    }

    public fun annotation(an: Text) {
        append("annotations", an)
    }

    public fun annotation(anBuilder: Text.() -> Unit) {
        annotation(Text(anBuilder))
    }

    public fun figure(sh: Shape) {
        append("shapes", sh)
    }

    public fun figure(shBuilder: Shape.() -> Unit) {
        figure(Shape(shBuilder))
    }

    public fun margin(block: Margin.() -> Unit) {
        margin = Margin(block)
    }

    public companion object : SchemeSpec<Layout>(::Layout)
}

