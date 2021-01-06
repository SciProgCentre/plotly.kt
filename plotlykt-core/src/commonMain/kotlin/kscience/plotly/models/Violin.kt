package kscience.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.values.Value
import kscience.plotly.listOfValues
import kscience.plotly.numberGreaterThan
import kscience.plotly.numberInRange
import kotlin.js.JsName

public enum class ViolinScaleMode {
    count,
    width
}

public enum class ViolinPoints {
    all,
    outliers,
    suspectedoutliers,
    `false`
}

public enum class ViolinSide {
    positive,
    negative,
    both
}

public enum class ViolinHoveron {
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

public class MeanLine : Scheme() {
    /**
     * Sets the mean line width.
     */
    public var width: Number by numberGreaterThan(0)

    /**
     * Sets the mean line color.
     */
    public val color: Color by color()

    /**
     * Determines if a line corresponding to the sample's
     * mean is shown inside the violins. If `box.visible` is
     * turned on, the mean line is drawn inside the inner box.
     * Otherwise, the mean line is drawn from one side of the violin to other.
     */
    public var visible: Boolean? by boolean()

    public companion object : SchemeSpec<MeanLine>(::MeanLine)
}

public enum class SpanMode {
    soft,
    hard,
    manual
}

public class ViolinBox : Scheme() {
    /**
     * Sets the width of the inner box plots relative to the violins' width.
     * For example, with 1, the inner box plots are as wide as the violins.
     * Default: 0.25.
     */
    public var width: Number by numberInRange(0.0..1.0)

    /**
     * Determines if an miniature box plot is drawn inside the violins.
     */
    public var visible: Boolean? by boolean()

    /**
     * Sets the inner box plot fill color.
     */
    public val fillcolor: Color by color()

    public companion object : SchemeSpec<ViolinBox>(::ViolinBox)
}

public class Violin : Trace() {
    init {
        type = TraceType.violin
    }

    /**
     * Sets the metric by which the width of each violin is determined.
     * "width" (default) means each violin has the same (max) width
     * "count" means the violins are scaled by the number
     * of sample points making up each violin.
     */
    public var scalemode: ViolinScaleMode by enum(ViolinScaleMode.width)

    /**
     * Determines on which side of the position value the density
     * function making up one half of a violin is plotted.
     * Useful when comparing two violin traces under "overlay" mode,
     * where one trace has `side` set to "positive" and the other to "negative".
     */
    public var side: ViolinSide by enum(ViolinSide.both)

    /**
     * Do the hover effects highlight individual violins or sample
     * points or the kernel density estimate or any combination of them?
     */
    public var hoveron: ViolinHoveron by enum(ViolinHoveron.`violins+points+kde`)

    public var meanline: MeanLine by spec(MeanLine)

    /**
     * If "outliers", only the sample points lying outside the whiskers
     * are shown If "suspectedoutliers", the outlier points are shown
     * and points either less than 4"Q1-3"Q3 or greater than 4"Q3-3"Q1
     * are highlighted (see `outliercolor`) If "all", all sample points
     * are shown If "false", only the violins are shown with no sample points.
     */
    public var points: ViolinPoints by enum(ViolinPoints.`false`)

    /**
     * Sets the method by which the span in data space where the density function will be computed.
     * "soft" means the span goes from the sample's minimum value minus two bandwidths to the sample's
     * maximum value plus two bandwidths. "hard" means the span goes from the sample's minimum to its maximum value.
     * For custom span settings, use mode "manual" and fill in the `span` attribute.
     */
    public var spanmode: SpanMode by enum(SpanMode.soft)

    /**
     * Sets the span in data space for which the density function will be computed.
     * Has an effect only when `spanmode` is set to "manual".
     */
    public var span: List<Value> by listOfValues()

    public var box: ViolinBox by spec(ViolinBox)

    /**
     * Sets the amount of jitter in the sample points drawn.
     * If "0", the sample points align along the distribution axis.
     * If "1", the sample points are drawn in a random jitter
     * of width equal to the width of the violins.
     */
    public var jitter: Number by numberInRange(0.0..1.0)

    /**
     * Sets the position of the sample points in relation to the violins.
     * If "0", the sample points are places over the center of the violins.
     * Positive (negative) values correspond to positions to the right (left)
     * for vertical violins and above (below) for horizontal violins.
     */
    public var pointpos: Number by numberInRange(-2.0..2.0)

    public fun meanline(block: MeanLine.() -> Unit) {
        meanline = MeanLine(block)
    }

    public fun box(block: ViolinBox.() -> Unit) {
        box = ViolinBox(block)
    }

    public fun span(array: Iterable<Any>) {
        span = array.map { Value.of(it) }
    }

    public companion object : SchemeSpec<Violin>(::Violin)
}