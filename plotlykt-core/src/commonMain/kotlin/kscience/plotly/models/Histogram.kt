package kscience.plotly.models

import hep.dataforge.meta.*
import kscience.plotly.intGreaterThan
import kscience.plotly.numberGreaterThan
import kotlin.js.JsName


public enum class HistFunc {
    count,
    sum,

    @JsName("avg")
    average,
    min,
    max
}

public enum class HistNorm {
    empty,
    percent,
    probability,
    density,

    @JsName("probabilityDensity")
    `probability density`
}

public class Bins : Scheme() {
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
    public var start: Number? by number()

    /**
     * Sets the end value for the x axis bins. The last bin may
     * not end exactly at this value, we increment the bin edge
     * by `size` from `start` until we reach or exceed `end`.
     * Defaults to the maximum data value. Like `start`, for
     * dates use a date string, and for category data `end`
     * is based on the category serial numbers.
     */
    public var end: Number? by number()

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
    public var size: Number by numberGreaterThan(0)

    public companion object : SchemeSpec<Bins>(::Bins)
}

public enum class HistogramDirection {
    increasing,
    decreasing
}

public enum class CurrentBin {
    include,
    exclude,
    half
}

public class Cumulative : Scheme() {
    /**
     * If true, display the cumulative distribution by summing
     * the binned values. Use the `direction` and `centralbin`
     * attributes to tune the accumulation method. Note: in this mode,
     * the "density" `histnorm` settings behave the same
     * as their equivalents without "density": "" and "density"
     * both rise to the number of data points, and "probability"
     * and "probability density" both rise to the number of sample points.
     */
    public var enabled: Boolean? by boolean()

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
    public var currentbin: CurrentBin by enum(CurrentBin.include)

    public companion object : SchemeSpec<Cumulative>(::Cumulative)
}


public open class Histogram : Trace() {
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
    public var histnorm: HistNorm by enum(HistNorm.empty)

    /**
     * Enumerated , one of ( "count" | "sum" | "avg" | "min" | "max" )
     * Specifies the binning function used for this histogram trace.
     * If "count", the histogram values are computed by counting the
     * number of values lying inside each bin. If "sum", "avg", "min", "max",
     * the histogram values are computed using the sum, the average,
     * the minimum or the maximum of the values lying inside each bin respectively.
     * Default: "count"
     */
    public var histfunc: HistFunc by enum(HistFunc.count)

    /**
     * Enumerated , one of ( "increasing" | "decreasing" )
     * Only applies if cumulative is enabled. If "increasing" (default)
     * we sum all prior bins, so the result increases from left to right.
     * If "decreasing" we sum later bins so the result decreases from left to right.
     * Default: increasing.
     */
    public var direction: HistogramDirection by enum(HistogramDirection.increasing)

    public var cumulative: Cumulative by spec(Cumulative)

    public var xbins: Bins by spec(Bins)

    public var ybins: Bins by spec(Bins)

    /**
     * Specifies the maximum number of desired bins. This value will be used in an algorithm
     * that will decide the optimal bin size such that the histogram best visualizes the
     * distribution of the data. Ignored if `xbins.size` is provided.
     */
    public var nbinsx: Int by intGreaterThan(0)

    /**
     * Specifies the maximum number of desired bins. This value will be used in an algorithm
     * that will decide the optimal bin size such that the histogram best visualizes the
     * distribution of the data. Ignored if `ybins.size` is provided.
     */
    public var nbinsy: Int by intGreaterThan(0)

    /**
     * Set a group of histogram traces which will have compatible bin settings.
     * Note that traces on the same subplot and with the same "orientation" under
     * `barmode` "stack", "relative" and "group" are forced into the same bingroup,
     * Using `bingroup`, traces under `barmode` "overlay" and on different axes
     * (of the same axis type) can have compatible bin settings. Note that histogram
     * and histogram2d" trace can share the same `bingroup`.
     * Default: "".
     */
    public var bingroup: String? by string()

    /**
     * Set a group of histogram traces which will have compatible x-bin settings.
     * Using `xbingroup`, histogram2d and histogram2dcontour traces (on axes of the same axis type)
     * can have compatible x-bin settings. Note that the same `xbingroup` value
     * can be used to set (1D) histogram `bingroup`
     */
    public var xbingroup: String? by string()

    /**
     * Set a group of histogram traces which will have compatible x-bin settings.
     * Using `ybingroup`, histogram2d and histogram2dcontour traces (on axes of the same axis type)
     * can have compatible x-bin settings. Note that the same `ybingroup` value
     * can be used to set (1D) histogram `bingroup`
     */
    public var ybingroup: String? by string()

    public fun cumulative(block: Cumulative.() -> Unit) {
        cumulative = Cumulative(block)
    }

    public fun xbins(block: Bins.() -> Unit) {
        xbins = Bins(block)
    }

    public fun ybins(block: Bins.() -> Unit) {
        ybins = Bins(block)
    }

    public companion object : SchemeSpec<Histogram>(::Histogram)
}

public class Histogram2D : Histogram(), Table2D {
    init {
        type = TraceType.histogram2d
    }

    /**
     * Sets the horizontal gap (in pixels) between bricks.
     */
    override var xgap: Number by numberGreaterThan(0)

    /**
     * Sets the vertical gap (in pixels) between bricks.
     */
    override var ygap: Number by numberGreaterThan(0)

    /**
     * Picks a smoothing algorithm use to smooth `z` data.
     */
    override var zsmooth: ZsmoothType by enum(ZsmoothType.best)

    public companion object : SchemeSpec<Histogram2D>(::Histogram2D)
}

public class Histogram2DContour : Histogram(), ContourSpec {
    init {
        type = TraceType.histogram2dcontour
    }

    /**
     * Sets the maximum number of contour levels. The actual number of contours
     * will be chosen automatically to be less than or equal to the value of `ncontours`.
     * Has an effect only if `autocontour` is "true" or if `contours.size` is missing.
     * Default: 15.
     */
    override var ncontours: Int by intGreaterThan(1)

    override var contours: Contours by spec(Contours)

    /**
     * Determines whether or not the contour level attributes are picked by an algorithm.
     * If "true" (default), the number of contour levels can be set in `ncontours`.
     * If "false", set the contour level attributes in `contours`.
     */
    override var autocontour: Boolean? by boolean()

    public fun contours(block: Contours.() -> Unit) {
        contours = Contours(block)
    }

    public companion object : SchemeSpec<Histogram2DContour>(::Histogram2DContour)
}