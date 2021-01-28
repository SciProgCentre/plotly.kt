package kscience.plotly.models

import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.boolean
import hep.dataforge.meta.enum
import hep.dataforge.meta.spec
import kscience.plotly.intGreaterThan

public class Contour : Trace(), HeatmapContour, ContourSpec {
    init {
        type = TraceType.contour
    }

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

    public companion object : SchemeSpec<Contour>(::Contour)
}