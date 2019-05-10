package scientifik.plotly.models.layout

import hep.dataforge.meta.Config
import hep.dataforge.meta.Specific
import hep.dataforge.meta.Specification
import hep.dataforge.meta.enum


enum class Shape {
    hv,
    vh,
    hvh,
    vhv,
    spline,
    linear
}

class Line(override val config: Config) : Specific {
    var shape by enum(Shape.linear)

    companion object : Specification<Line> {
        override fun wrap(config: Config): Line =
            Line(config)
    }
}