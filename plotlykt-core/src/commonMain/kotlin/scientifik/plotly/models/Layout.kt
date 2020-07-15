package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.doubleInRange
import scientifik.plotly.intGreaterThan
import scientifik.plotly.lazySpec
import scientifik.plotly.list


enum class BarMode {
    stack,
    group,
    overlay,
    relative
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
    var title by string()

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
    var bargap by doubleInRange(0.0..1.0)

    /**
     * Sets the gap (in plot fraction) between bars of the same location coordinate.
     * Default: 0.
     */
    var bargroupgap by doubleInRange(0.0..1.0)

    var legend by lazySpec(Legend)

    /**
     * An annotation is a text element that can be placed anywhere in the plot.
     * It can be positioned with respect to relative coordinates in the plot
     * or with respect to the actual data coordinates of the graph.
     * Annotations can be shown with or without an arrow.
     */
    var annotations by list(Annotation)

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

    fun annotation(an: Annotation) {
        config.append("annotations", an)
    }

    fun annotation(anBuilder: Annotation.() -> Unit) {
        annotation(Annotation(anBuilder))
    }

    companion object : SchemeSpec<Layout>(::Layout)
}

