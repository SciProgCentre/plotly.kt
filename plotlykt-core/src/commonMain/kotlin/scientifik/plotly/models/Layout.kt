package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import scientifik.plotly.*
import kotlin.js.JsName


enum class BarMode {
    stack,
    group,
    overlay,
    relative
}

class Margin : Scheme() {
    /**
     * Turns on/off margin expansion computations. Legends, colorbars, updatemenus, sliders,
     * axis rangeselector and rangeslider are allowed to push the margins by defaults.
     */
    var autoexpand by boolean()

    /**
     * Sets the amount of padding (in px) between the plotting area and the axis lines
     */
    var pad by intGreaterThan(0)

    /**
     * Sets the left margin (in px). Default: 80.
     */
    var l by intGreaterThan(0)

    /**
     * Sets the right margin (in px). Default: 80.
     */
    var r by intGreaterThan(0)

    /**
     * Sets the top margin (in px). Default: 100.
     */
    var t by intGreaterThan(0)

    /**
     * Sets the bottom margin (in px). Default: 80.
     */
    var b by intGreaterThan(0)

    companion object : SchemeSpec<Margin>(::Margin)
}

enum class BoxMode {
    overlay,
    group
}

enum class HoverMode {
    x,
    y,
    closest,
    `false`,
    @JsName("xUnified")
    `x unified`,
    @JsName("yUnified")
    `y unified`
}

class Layout : Scheme() {
    /**
     * Sets the plot's width (in px).
     * Number greater than or equal to 10, default: 700.
     */
    var width by intGreaterThan(10)

    /**
     * Sets the plot's height (in px).
     * Number greater than or equal to 10, default: 450.
     */
    var height by intGreaterThan(10)

    /**
     * Determines whether or not a layout width or height that has been left undefined
     * by the user is initialized on each relayout. Note that, regardless of this attribute, an undefined layout
     * width or height is always initialized on the first call to plot.
     */
    var autosize by boolean()

    /**
     * Sets the plot's title.
     */
    var title: String?
        get() = config["title.text"].string ?: config["title"].string
        set(value) {
            config["title.text"] = value
        }

    var xaxis by lazySpec(Axis)

    var yaxis by lazySpec(Axis)

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
    var barmode: BarMode by enum(BarMode.group)

    /**
     * Sets the gap (in plot fraction) between bars
     * of adjacent location coordinates.
     */
    var bargap by numberInRange(0.0..1.0)

    /**
     * Sets the gap (in plot fraction) between bars of the same location coordinate.
     * Default: 0.
     */
    var bargroupgap by numberInRange(0.0..1.0)

    var legend by lazySpec(Legend)

    /**
     * An annotation is a text element that can be placed anywhere in the plot.
     * It can be positioned with respect to relative coordinates in the plot
     * or with respect to the actual data coordinates of the graph.
     * Annotations can be shown with or without an arrow.
     */
    var annotations by list(Text)

    var shapes by list(Shape)

    /**
     * Sets the background color of the paper where the graph is drawn.
     * Default: #fff.
     */
    var paper_bgcolor = Color(this, "paper_bgcolor".asName())

    /**
     * Sets the background color of the plotting area in-between x and y axes.
     * Default: #fff.
     */
    var plot_bgcolor = Color(this, "plot_bgcolor".asName())

    var margin by spec(Margin)

    /**
     * Determines how boxes at the same location coordinate are displayed on the graph. If "group",
     * the boxes are plotted next to one another centered around the shared location. If "overlay",
     * the boxes are plotted over one another, you might need to set "opacity" to see them multiple boxes.
     * Has no effect on traces that have "width" set.
     */
    var boxmode by enum(BoxMode.overlay)

    /**
     * Determines whether or not a legend is drawn. Default is `true` if there is a trace to show and any of these:
     * a) Two or more traces would by default be shown in the legend.
     * b) One pie trace is shown in the legend.
     * c) One trace is explicitly given with `showlegend: true`.
     */
    var showlegend by boolean()

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
    var hovermode by enum(HoverMode.closest)

    fun legend(block: Legend.() -> Unit) {
        legend.apply(block)
    }

    fun title(block: Title.() -> Unit) {
        val spec = config["title"].node?.let { Title.wrap(it) }
            ?: Title.empty().also { config["title"] = it.config }
        spec.apply(block)
    }

    //TODO moe title to parameter block
    fun xaxis(block: Axis.() -> Unit) {
        xaxis.apply(block)
    }

    fun yaxis(block: Axis.() -> Unit) {
        yaxis.apply(block)
    }

    fun annotation(an: Text) {
        config.append("annotations", an)
    }

    fun annotation(anBuilder: Text.() -> Unit) {
        annotation(Text(anBuilder))
    }

    fun figure(sh: Shape) {
        config.append("shapes", sh)
    }

    fun figure(shBuilder: Shape.() -> Unit) {
        figure(Shape(shBuilder))
    }

    fun margin(block: Margin.() -> Unit) {
        margin = Margin(block)
    }

    companion object : SchemeSpec<Layout>(::Layout)
}

