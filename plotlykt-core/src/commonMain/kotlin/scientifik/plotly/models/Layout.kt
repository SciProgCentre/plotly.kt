package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.models.layout.Axis
import scientifik.plotly.models.layout.Legend


enum class BarMode{
    stack,
    group,
    overlay,
    relative
}

class Layout(override val config: Config) : Specific {
    var title by string()
    var xaxis by spec(Axis)
    var yaxis by spec(Axis)
    var barmode by enum(BarMode.group)
    var bargap by double() // FIXME("number between or equal to 0 and 1")
    var bargroupgap by double() // FIXME("number between or equal to 0 and 1")
    var legend by spec(Legend)

    fun legend(block: Legend.() -> Unit) {
        legend = Legend.build(block)
    }

    //TODO moe title to parameter block
    fun xaxis(block: Axis.() -> Unit) {
        xaxis = Axis.build(block)
    }

    fun yaxis(block: Axis.() -> Unit) {
        yaxis = Axis.build(block)
    }

    companion object : Specification<Layout> {
        override fun wrap(config: Config): Layout = Layout(config)
    }
}

