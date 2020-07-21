package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import scientifik.plotly.*


enum class BarMode {
    stack,
    group,
    overlay,
    relative
}

class Margin : Scheme() {
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

    fun margin(block: Margin.() -> Unit) {
        margin = Margin(block)
    }

    companion object : SchemeSpec<Layout>(::Layout)
}

