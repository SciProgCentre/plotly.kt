package scientifik.plotly.models

import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.enum


class Histogram() : Trace() {
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

    companion object : SchemeSpec<Histogram>(::Histogram) {
        const val X_AXIS = "x"
        const val Y_AXIS = "y"
        const val TEXT_AXIS = "text"

        operator fun invoke(xs: Any, ys: Any? = null/*, zs: Any? = null*/, block: Histogram.() -> Unit = {}) = invoke {
            type = TraceType.histogram
            block()
            x.set(xs)
            y.set(ys)
        }
    }
}