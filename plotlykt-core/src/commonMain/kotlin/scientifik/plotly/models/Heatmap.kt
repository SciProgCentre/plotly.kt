package scientifik.plotly.models
import hep.dataforge.meta.*
import scientifik.plotly.intGreaterThan
import scientifik.plotly.numberGreaterThan

open class Heatmap(): Trace(), Table2D, HeatmapContour {
    init {
        type = TraceType.heatmap
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

    /**
     * If "array", the heatmap's x coordinates are given by "x" (the default behavior when `x` is provided).
     * If "scaled", the heatmap's x coordinates are given by "x0" and "dx" (the default behavior when `x` is not provided).
     */
    override  var xtype by enum(DataType.array)

    /**
     * If "array", the heatmap's y coordinates are given by "y" (the default behavior when `y` is provided)
     * If "scaled", the heatmap's y coordinates are given by "y0" and "dy" (the default behavior when `y` is not provided)
     */
    override var ytype by enum(DataType.array)

    companion object : SchemeSpec<Heatmap>(::Heatmap)
}

class HeatmapGL(): Heatmap() {
    init {
        type = TraceType.heatmapgl
    }

    companion object : SchemeSpec<HeatmapGL>(::HeatmapGL)
}