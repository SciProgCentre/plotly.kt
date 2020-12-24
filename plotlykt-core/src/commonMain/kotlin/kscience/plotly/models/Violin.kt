package kscience.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import kscience.plotly.listOfValues
import kscience.plotly.numberGreaterThan
import kscience.plotly.numberInRange
import kotlin.js.JsName

enum class ViolinScaleMode {
    count,
    width
}

enum class ViolinPoints {
    all,
    outliers,
    suspectedoutliers,
    `false`
}

enum class ViolinSide {
    positive,
    negative,
    both
}

enum class ViolinHoveron {
    violins,
    points,
    kde,

    @JsName("violinsAndPoints")
    `violins+points`,

    @JsName("violinsAndKde")
    `violins+kde`,

    @JsName("pointsAndKde")
    `points+kde`,

    @JsName("violinsAndPointsAndKde")
    `violins+points+kde`,
    all
}

class MeanLine : Scheme() {
    /**
     * Sets the mean line width.
     */
    var width by numberGreaterThan(0)

    /**
     * Sets the mean line color.
     */
    var color = Color(this, "color".asName())

    /**
     * Determines if a line corresponding to the sample's
     * mean is shown inside the violins. If `box.visible` is
     * turned on, the mean line is drawn inside the inner box.
     * Otherwise, the mean line is drawn from one side of the violin to other.
     */
    var visible by boolean()

    companion object : SchemeSpec<MeanLine>(::MeanLine)
}

enum class SpanMode {
    soft,
    hard,
    manual
}

class ViolinBox : Scheme() {
    /**
     * Sets the width of the inner box plots relative to the violins' width.
     * For example, with 1, the inner box plots are as wide as the violins.
     * Default: 0.25.
     */
    var width by numberInRange(0.0..1.0)

    /**
     * Determines if an miniature box plot is drawn inside the violins.
     */
    var visible by boolean()

    /**
     * Sets the inner box plot fill color.
     */
    var fillcolor = Color(this, "fillcolor".asName())

    companion object : SchemeSpec<ViolinBox>(::ViolinBox)
}

class Violin : Trace() {
    init {
        type = TraceType.violin
    }

    /**
     * Sets the metric by which the width of each violin is determined.
     * "width" (default) means each violin has the same (max) width
     * "count" means the violins are scaled by the number
     * of sample points making up each violin.
     */
    var scalemode by enum(ViolinScaleMode.width)

    /**
     * Determines on which side of the position value the density
     * function making up one half of a violin is plotted.
     * Useful when comparing two violin traces under "overlay" mode,
     * where one trace has `side` set to "positive" and the other to "negative".
     */
    var side by enum(ViolinSide.both)

    /**
     * Do the hover effects highlight individual violins or sample
     * points or the kernel density estimate or any combination of them?
     */
    var hoveron by enum(ViolinHoveron.`violins+points+kde`)

    var meanline by spec(MeanLine)

    /**
     * If "outliers", only the sample points lying outside the whiskers
     * are shown If "suspectedoutliers", the outlier points are shown
     * and points either less than 4"Q1-3"Q3 or greater than 4"Q3-3"Q1
     * are highlighted (see `outliercolor`) If "all", all sample points
     * are shown If "false", only the violins are shown with no sample points.
     */
    var points by enum(ViolinPoints.`false`)

    /**
     * Sets the method by which the span in data space where the density function will be computed.
     * "soft" means the span goes from the sample's minimum value minus two bandwidths to the sample's
     * maximum value plus two bandwidths. "hard" means the span goes from the sample's minimum to its maximum value.
     * For custom span settings, use mode "manual" and fill in the `span` attribute.
     */
    var spanmode by enum(SpanMode.soft)

    /**
     * Sets the span in data space for which the density function will be computed.
     * Has an effect only when `spanmode` is set to "manual".
     */
    var span by listOfValues()

    var box by spec(ViolinBox)

    /**
     * Sets the amount of jitter in the sample points drawn.
     * If "0", the sample points align along the distribution axis.
     * If "1", the sample points are drawn in a random jitter
     * of width equal to the width of the violins.
     */
    var jitter by numberInRange(0.0..1.0)

    /**
     * Sets the position of the sample points in relation to the violins.
     * If "0", the sample points are places over the center of the violins.
     * Positive (negative) values correspond to positions to the right (left)
     * for vertical violins and above (below) for horizontal violins.
     */
    var pointpos by numberInRange(-2.0..2.0)

    fun meanline(block: MeanLine.() -> Unit) {
        meanline = MeanLine(block)
    }

    fun box(block: ViolinBox.() -> Unit) {
        box = ViolinBox(block)
    }

    fun span(array: Iterable<Any>) {
        span = array.map { Value.of(it) }
    }

    companion object : SchemeSpec<Violin>(::Violin)
}