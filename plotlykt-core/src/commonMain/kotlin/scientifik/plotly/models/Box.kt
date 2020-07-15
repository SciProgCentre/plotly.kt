package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import scientifik.plotly.doubleInRange
import scientifik.plotly.intGreaterThan
import scientifik.plotly.list

enum class BoxMean {
    `true`,
    sd,
    `false`
}

enum class BoxPoints {
    all,
    outliers,
    suspectedoutliers,
    `false`
}

enum class QuartileMethod {
    linear,
    exclusive,
    inclusive
}

enum class BoxHoveron {
    boxes,
    points,
    `boxes+points`
}

class Box(): Trace(), SelectedPoints {
    /**
     * Sets the width of the box in data coordinate If "0" (default value) the width is
     * automatically selected based on the positions of other box traces in the same subplot.
     */
    var width by intGreaterThan(0)

    /**
     * If "true", the mean of the box(es)' underlying distribution is drawn as a dashed line
     * inside the box(es). If "sd" the standard deviation is also drawn. Defaults to "true"
     * when `mean` is set. Defaults to "sd" when `sd` is set Otherwise defaults to "false".
     */
    var boxmean by enum(BoxMean.`false`)

    /**
     * If "outliers", only the sample points lying outside the whiskers are shown
     * If "suspectedoutliers", the outlier points are shown and points either less
     * than 4"Q1-3"Q3 or greater than 4"Q3-3"Q1 are highlighted (see `outliercolor`)
     * If "all", all sample points are shown If "false", only the box(es) are shown
     * with no sample points Defaults to "suspectedoutliers" when `marker.outliercolor`
     * or `marker.line.outliercolor` is set. Defaults to "all" under the q1/median/q3 signature.
     * Otherwise defaults to "outliers".
     */
    var boxpoints by enum(BoxPoints.all)

    /**
     * Determines whether or not notches are drawn. Notches displays a confidence interval
     * around the median. We compute the confidence interval as median +/- 1.57 " IQR / sqrt(N),
     * where IQR is the interquartile range and N is the sample size. If two boxes' notches
     * do not overlap there is 95% confidence their medians differ.
     * See https://sites.google.com/site/davidsstatistics/home/notched-box-plots for more info.
     * Defaults to "false" unless `notchwidth` or `notchspan` is set.
     */
    var notched by boolean()

    /**
     * Sets the width of the notches relative to the box' width.
     * For example, with 0, the notches are as wide as the box(es).
     * Default: 0.25.
     */
    var notchwidth by doubleInRange(0.0..0.5)

    /**
     * Sets the notch span from the boxes' `median` values. There should be as many items
     * as the number of boxes desired. This attribute has effect only under the q1/median/q3 signature.
     * If `notchspan` is not provided but a sample (in `y` or `x`) is set, we compute it
     * as 1.57 " IQR / sqrt(N), where N is the sample size.
     */
    var notchspan by list()

    /**
     * Sets the width of the whiskers relative to the box' width. For example, with 1,
     * the whiskers are as wide as the box(es). Default: 0.5.
     */
    var whiskerwidth by doubleInRange(0.0..1.0)

    /**
     * Sets the Quartile 1 values. There should be as many items as the number of boxes desired.
     */
    var q1 by list()

    /**
     * Sets the Quartile 3 values. There should be as many items as the number of boxes desired.
     */
    var q3 by list()

    /**
     * Sets the median values. There should be as many items as the number of boxes desired.
     */
    var median by list()

    /**
     * Sets the mean values. There should be as many items as the number of boxes desired.
     * This attribute has effect only under the q1/median/q3 signature. If `mean` is not provided
     * but a sample (in `y` or `x`) is set, we compute the mean for each box using the sample values.
     */
    var mean by list()

    /**
     * Sets the standard deviation values. There should be as many items as the number of boxes desired.
     * This attribute has effect only under the q1/median/q3 signature. If `sd` is not provided
     * but a sample (in `y` or `x`) is set, we compute the standard deviation for each box using the sample values.

     */
    var sd by list()

    /**
     * Sets the lower fence values. There should be as many items as the number of boxes desired.
     * This attribute has effect only under the q1/median/q3 signature. If `lowerfence` is not provided
     * but a sample (in `y` or `x`) is set, we compute the lower as the last sample point below 1.5 times the IQR.
     */
    var lowerfence by list()

    /**
     * Sets the upper fence values. There should be as many items as the number of boxes desired.
     * This attribute has effect only under the q1/median/q3 signature. If `upperfence` is not provided
     * but a sample (in `y` or `x`) is set, we compute the lower as the last sample point above 1.5 times the IQR.
     */
    var upperfence by list()

    /**
     * Sets the method used to compute the sample's Q1 and Q3 quartiles. The "linear" method uses
     * the 25th percentile for Q1 and 75th percentile for Q3 as computed using method #10
     * (listed on http://www.amstat.org/publications/jse/v14n3/langford.html). The "exclusive" method
     * uses the median to divide the ordered dataset into two halves if the sample is odd,
     * it does not include the median in either half - Q1 is then the median of the lower half and Q3
     * the median of the upper half. The "inclusive" method also uses the median to divide the ordered dataset
     * into two halves but if the sample is odd, it includes the median in both halves - Q1 is then the median
     * of the lower half and Q3 the median of the upper half.
     */
    var quartilemethod by enum(QuartileMethod.linear)

    /**
     * Array containing integer indices of selected points. Has an effect only for traces that support selections.
     * Note that an empty array means an empty selection where the `unselected` are turned on for all points, whereas,
     * any other non-array values means no selection all where the `selected` and `unselected` styles have no effect.
     */
    override var selectedpoints by numberList()

    override var selected by spec(SelectPoints)

    override var unselected by spec(SelectPoints)

    /**
     * Sets the amount of jitter in the sample points drawn.
     * If "0", the sample points align along the distribution axis.
     * If "1", the sample points are drawn in a random jitter
     * of width equal to the width of the violins.
     */
    var jitter by doubleInRange(0.0..1.0)

    /**
     * Sets the position of the sample points in relation to the violins.
     * If "0", the sample points are places over the center of the violins.
     * Positive (negative) values correspond to positions to the right (left)
     * for vertical violins and above (below) for horizontal violins.
     */
    var pointpos by doubleInRange(-2.0..2.0)

    /**
     * Do the hover effects highlight individual boxes or sample points or both?
     */
    var hoveron by enum(BoxHoveron.`boxes+points`)

    fun q1(array: Iterable<Any>) {
        q1 = array.map{ Value.of(it) }
    }

    fun q3(array: Iterable<Any>) {
        q3 = array.map{ Value.of(it) }
    }

    fun median(array: Iterable<Any>) {
        median = array.map{ Value.of(it) }
    }

    fun lowerfence(array: Iterable<Any>) {
        lowerfence = array.map{ Value.of(it) }
    }

    fun upperfence(array: Iterable<Any>) {
        upperfence = array.map{ Value.of(it) }
    }

    fun notchspan(array: Iterable<Any>) {
        notchspan = array.map{ Value.of(it) }
    }

    fun mean(array: Iterable<Any>) {
        mean = array.map{ Value.of(it) }
    }

    fun sd(array: Iterable<Any>) {
        sd = array.map{ Value.of(it) }
    }

    fun selected(block: SelectPoints.() -> Unit) {
        selected = SelectPoints(block)
    }

    fun unselected(block: SelectPoints.() -> Unit) {
        unselected = SelectPoints(block)
    }

    companion object : SchemeSpec<Box>(::Box)
}