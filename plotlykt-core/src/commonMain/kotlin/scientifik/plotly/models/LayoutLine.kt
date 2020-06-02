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
    var shape by enum(Shape.linear)

    companion object : SchemeSpec<LayoutLine>(::LayoutLine)
}