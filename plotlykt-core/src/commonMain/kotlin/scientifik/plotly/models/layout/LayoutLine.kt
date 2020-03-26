package scientifik.plotly.models.layout

import hep.dataforge.meta.scheme.Scheme
import hep.dataforge.meta.scheme.SchemeSpec
import hep.dataforge.meta.scheme.enum


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