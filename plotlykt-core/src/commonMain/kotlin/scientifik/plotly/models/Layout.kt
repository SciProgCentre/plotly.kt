package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.lazySpec
import scientifik.plotly.list


enum class BarMode {
    stack,
    group,
    overlay,
    relative
}

class Layout : Scheme() {
    var title by string()
    var xaxis by lazySpec(Axis)
    var yaxis by lazySpec(Axis)
    var barmode: BarMode by enum(BarMode.group)
    var bargap by double() // FIXME("number between or equal to 0 and 1")
    var bargroupgap by double() // FIXME("number between or equal to 0 and 1")
    var legend by lazySpec(Legend)
    var annotations by list(Annotation)

    fun legend(block: Legend.() -> Unit) {
        legend.apply(block)
    }

    //TODO moe title to parameter block
    fun xaxis(block: Axis.() -> Unit) {
        xaxis.apply(block)
    }

    fun yaxis(block: Axis.() -> Unit) {
        yaxis.apply(block)
    }

    fun annotation(an: Annotation) {
        config.append("annotations", an)
    }

    fun annotation(anBuilder: Annotation.() -> Unit) {
        annotation(Annotation(anBuilder))
    }

    companion object : SchemeSpec<Layout>(::Layout)
}

