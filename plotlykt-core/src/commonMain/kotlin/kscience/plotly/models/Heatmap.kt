package kscience.plotly.models

import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.enum
import kscience.plotly.numberGreaterThan

public open class Heatmap : Trace(), Table2D, HeatmapContour {
    init {
        type = TraceType.heatmap
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

    /**
     * If "array", the heatmap's x coordinates are given by "x" (the default behavior when `x` is provided).
     * If "scaled", the heatmap's x coordinates are given by "x0" and "dx" (the default behavior when `x` is not provided).
     */
    override var xtype: DataType by enum(DataType.array)

    /**
     * If "array", the heatmap's y coordinates are given by "y" (the default behavior when `y` is provided)
     * If "scaled", the heatmap's y coordinates are given by "y0" and "dy" (the default behavior when `y` is not provided)
     */
    override var ytype: DataType by enum(DataType.array)

    public companion object : SchemeSpec<Heatmap>(::Heatmap)
}

public class HeatmapGL : Heatmap() {
    init {
        type = TraceType.heatmapgl
    }

    public companion object : SchemeSpec<HeatmapGL>(::HeatmapGL)
}