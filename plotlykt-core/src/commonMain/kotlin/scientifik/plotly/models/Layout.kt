package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.models.layout.Axis

class Layout(override val config: Config) : Specific {
    var title by string()
    var xaxis by spec(Axis)
    var yaxis by spec(Axis)

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

