package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.list
import scientifik.plotly.models.layout.Annotation
import scientifik.plotly.models.layout.Axis
import scientifik.plotly.models.layout.Legend


enum class BarMode {
    stack,
    group,
    overlay,
    relative
}

class Layout : Scheme() {
    var title by string()
    var xaxis by spec(Axis)
    var yaxis by spec(Axis)
    var barmode: BarMode by enum(BarMode.group)
    var bargap by double() // FIXME("number between or equal to 0 and 1")
    var bargroupgap by double() // FIXME("number between or equal to 0 and 1")
    var legend by spec(Legend)
    var annotations by list(Annotation)

    fun legend(block: Legend.() -> Unit) {
        legend = Legend(block)
    }

    //TODO moe title to parameter block
    fun xaxis(block: Axis.() -> Unit) {
        xaxis = Axis(block)
    }

    fun yaxis(block: Axis.() -> Unit) {
        yaxis = Axis(block)
    }

    fun annotation(an: Annotation) {
        config.append("annotations", an)
    }

    fun annotation(anBuilder: Annotation.() -> Unit) {
        annotation(Annotation(anBuilder))
    }

    companion object : SchemeSpec<Layout>(::Layout)
}

