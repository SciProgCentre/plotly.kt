package scientifik.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.enum

enum class Shape {
    hv,
    vh,
    hvh,
    vhv,
    spline,
    linear
}

class LayoutLine : Scheme() {
    /**
     * Determines the line shape. With "spline" the lines
     * are drawn using spline interpolation. The other
     * available values correspond to step-wise line shapes.
     * Default: "linear"
     */
    var shape by enum(Shape.linear)

    companion object : SchemeSpec<LayoutLine>(::LayoutLine)
}