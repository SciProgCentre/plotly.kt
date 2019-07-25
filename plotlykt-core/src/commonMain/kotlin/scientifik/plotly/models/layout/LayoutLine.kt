package scientifik.plotly.models.layout

import hep.dataforge.meta.Config
import hep.dataforge.meta.Specification
import hep.dataforge.meta.enum
import scientifik.plotly.models.general.Line


enum class Shape {
    hv,
    vh,
    hvh,
    vhv,
    spline,
    linear
}

class LayoutLine(override val config: Config) : Line() {
    var shape by enum(Shape.linear)

    companion object : Specification<LayoutLine> {
        override fun wrap(config: Config): LayoutLine =
            LayoutLine(config)
    }
}