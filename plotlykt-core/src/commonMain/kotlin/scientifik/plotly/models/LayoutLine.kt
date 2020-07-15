package scientifik.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.enum
import hep.dataforge.names.asName
import scientifik.plotly.doubleInRange
import scientifik.plotly.intGreaterThan

enum class Shape {
    hv,
    vh,
    hvh,
    vhv,
    spline,
    linear
}

enum class Dash {
    solid,
    dot,
    dash,
    longdash,
    dashdot,
    longdashdot
}

class LayoutLine : Scheme(), Line {
    /**
     * Sets the line color.
     */
    override val color = Color(this, "color".asName())

    /**
     * Sets the line width (in px). Default: 2.
     */
    override var width by intGreaterThan(0)

    /**
     * Determines the line shape. With "spline" the lines
     * are drawn using spline interpolation. The other
     * available values correspond to step-wise line shapes.
     * Default: "linear"
     */
    var shape by enum(Shape.linear)

    /**
     * Sets the style of the lines.
     */
    var dash by enum(Dash.solid)

    /**
     * Sets the amount of smoothing for the contour lines, where "0" corresponds to no smoothing.
     */
    var smoothing by doubleInRange(0.0..1.3)

    companion object : SchemeSpec<LayoutLine>(::LayoutLine)
}