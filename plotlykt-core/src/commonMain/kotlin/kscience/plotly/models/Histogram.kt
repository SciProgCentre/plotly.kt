package kscience.plotly.models

import hep.dataforge.meta.*
import kscience.plotly.intGreaterThan
import kscience.plotly.numberGreaterThan
import kotlin.js.JsName


enum class HistFunc {
    count,
    sum,

    @JsName("avg")
    average,
    min,
    max
}

enum class HistNorm {
    empty,
    percent,
    probability,
    density,

    @JsName("probabilityDensity")
    `probability density`
}

class Bins : Scheme() {
    //FIXME("add categorical coordinate string")

    /**
     * Sets the starting value for the x axis bins.
     * Defaults to the minimum data value, shifted down
     * if necessary to make nice round values and to remove
     * ambiguous bin edges. For example, if most of the data
     * is integers we shift the bin edges 0.5 down, so a `size`
     * of 5 would have a default `start` of -0.5, so it is clear
     * that 0-4 are in the first bin, 5-9 in the second, but
     * continuous data gets a start of 0 and bins [0,5), [5,10) etc.
     * Dates behave similarly, and `start` should be a date string.
     * For category data, `start` is based on the category
     * serial numbers, and defaults to -0.5.
     */
    var start by number()

    /**
     * Sets the end value for the x axis bins. The last bin may
     * not end exactly at this value, we increment the bin edge
     * by `size` from `start` until we reach or exceed `end`.
     * Defaults to the maximum data value. Like `start`, for
     * dates use a date string, and for category data `end`
     * is based on the category serial numbers.
     */
    var end by number()

    /**
     * Sets the size of each x axis bin. Default behavior:
     * If `nbinsx` is 0 or omitted, we choose a nice round
     * bin size such that the number of bins is about
     * the same as the typical number of samples in each bin.
     * If `nbinsx` is provided, we choose a nice round bin size
     * giving no more than that many bins. For date data,
     * use milliseconds or "M<n>" for months, as in `axis.dtick`.
     * For category data, the number of categories to bin together
     * (always defaults to 1).
     */
    var size by numberGreaterThan(0)

    companion object : SchemeSpec<Bins>(::Bins)
}

enum class HistogramDirection {
    increasing,
    decreasing
}

enum class CurrentBin {
    include,
    exclude,
    half
}

class Cumulative : Scheme() {
    /**
     * If true, display the cumulative distribution by summing
     * the binned values. Use the `direction` and `centralbin`
     * attributes to tune the accumulation method. Note: in this mode,
     * the "density" `histnorm` settings behave the same
     * as their equivalents without "density": "" and "density"
     * both rise to the number of data points, and "probability"
     * and "probability density" both rise to the number of sample points.
     */
    var enabled by boolean()

    /**
     * Enumerated , one of ( "include" | "exclude" | "half" )
     * Only applies if cumulative is enabled. Sets whether the current bin
     * is included, excluded, or has half of its value included in
     * the current cumulative value. "include" is the default for
     * compatibility with various other tools, however it introduces
     * a half-bin bias to the results. "exclude" makes the opposite
     * half-bin bias, and "half" removes it.
     * Default: include.
     */
    var currentbin by enum(CurrentBin.include)

    companion object : SchemeSpec<Cumulative>(::Cumulative)
}


open class Histogram : Trace() {
    init {
        type = TraceType.histogram
    }

    /**
     * Enumerated, one of ( "empty" | "percent" | "probability" | "density" | "probability density" )
     * Specifies the type of normalization used for this histogram trace.
     * If "empty", the span of each bar corresponds to the number of occurrences
     * (i.e. the number of data points lying inside the bins). If "percent" / "probability",
     * the span of each bar corresponds to the percentage / fraction of occurrences
     * with respect to the total number of sample points (here, the sum of all
     * bin HEIGHTS equals 100% / 1). If "density", the span of each bar corresponds
     * to the number of occurrences in a bin divided by the size of the bin interval
     * (here, the sum of all bin AREAS equals the total number of sample points).
     * If "probability density", the area of each bar corresponds to the probability
     * that an event will fall into the corresponding bin (here, the sum of all bin AREAS equals 1).
     * Default: "empty".
     */
    var histnorm by enum(HistNorm.empty)

    /**
     * Enumerated , one of ( "count" | "sum" | "avg" | "min" | "max" )
     * Specifies the binning function used for this histogram trace.
     * If "count", the histogram values are computed by counting the
     * number of values lying inside each bin. If "sum", "avg", "min", "max",
     * the histogram values are computed using the sum, the average,
     * the minimum or the maximum of the values lying inside each bin respectively.
     * Default: "count"
     */
    var histfunc by enum(HistFunc.count)

    /**
     * Enumerated , one of ( "increasing" | "decreasing" )
     * Only applies if cumulative is enabled. If "increasing" (default)
     * we sum all prior bins, so the result increases from left to right.
     * If "decreasing" we sum later bins so the result decreases from left to right.
     * Default: increasing.
     */
    var direction by enum(HistogramDirection.increasing)

    var cumulative by spec(Cumulative)

    var xbins by spec(Bins)

    var ybins by spec(Bins)

    /**
     * Specifies the maximum number of desired bins. This value will be used in an algorithm
     * that will decide the optimal bin size such that the histogram best visualizes the
     * distribution of the data. Ignored if `xbins.size` is provided.
     */
    var nbinsx by intGreaterThan(0)

    /**
     * Specifies the maximum number of desired bins. This value will be used in an algorithm
     * that will decide the optimal bin size such that the histogram best visualizes the
     * distribution of the data. Ignored if `ybins.size` is provided.
     */
    var nbinsy by intGreaterThan(0)

    /**
     * Set a group of histogram traces which will have compatible bin settings.
     * Note that traces on the same subplot and with the same "orientation" under
     * `barmode` "stack", "relative" and "group" are forced into the same bingroup,
     * Using `bingroup`, traces under `barmode` "overlay" and on different axes
     * (of the same axis type) can have compatible bin settings. Note that histogram
     * and histogram2d" trace can share the same `bingroup`.
     * Default: "".
     */
    var bingroup by string()

    /**
     * Set a group of histogram traces which will have compatible x-bin settings.
     * Using `xbingroup`, histogram2d and histogram2dcontour traces (on axes of the same axis type)
     * can have compatible x-bin settings. Note that the same `xbingroup` value
     * can be used to set (1D) histogram `bingroup`
     */
    var xbingroup by string()

    /**
     * Set a group of histogram traces which will have compatible x-bin settings.
     * Using `ybingroup`, histogram2d and histogram2dcontour traces (on axes of the same axis type)
     * can have compatible x-bin settings. Note that the same `ybingroup` value
     * can be used to set (1D) histogram `bingroup`
     */
    var ybingroup by string()

    fun cumulative(block: Cumulative.() -> Unit) {
        cumulative = Cumulative(block)
    }

    fun xbins(block: Bins.() -> Unit) {
        xbins = Bins(block)
    }

    fun ybins(block: Bins.() -> Unit) {
        ybins = Bins(block)
    }

    companion object : SchemeSpec<Histogram>(::Histogram)
}

class Histogram2D : Histogram(), Table2D {
    init {
        type = TraceType.histogram2d
    }

    /**
     * Sets the horizontal gap (in pixels) between bricks.
     */
    override var xgap by numberGreaterThan(0)

    /**
     * Sets the vertical gap (in pixels) between bricks.
     */
    override var ygap by numberGreaterThan(0)

    /**
     * Picks a smoothing algorithm use to smooth `z` data.
     */
    override var zsmooth by enum(ZsmoothType.best)

    companion object : SchemeSpec<Histogram2D>(::Histogram2D)
}

class Histogram2DContour : Histogram(), ContourSpec {
    init {
        type = TraceType.histogram2dcontour
    }

    /**
     * Sets the maximum number of contour levels. The actual number of contours
     * will be chosen automatically to be less than or equal to the value of `ncontours`.
     * Has an effect only if `autocontour` is "true" or if `contours.size` is missing.
     * Default: 15.
     */
    override var ncontours by intGreaterThan(1)

    override var contours by spec(Contours)

    /**
     * Determines whether or not the contour level attributes are picked by an algorithm.
     * If "true" (default), the number of contour levels can be set in `ncontours`.
     * If "false", set the contour level attributes in `contours`.
     */
    override var autocontour by boolean()

    fun contours(block: Contours.() -> Unit) {
        contours = Contours(block)
    }

    companion object : SchemeSpec<Histogram2DContour>(::Histogram2DContour)
}