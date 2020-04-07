package scientifik.plotly.models.layout

import hep.dataforge.meta.*
import hep.dataforge.values.asValue
import scientifik.plotly.models.Font

/**
 * Text annotation
 */
class Annotation : Scheme() {
    var visible by boolean(true)
    var text by string("")
    var font by spec(Font)
    var x by value()
    var y by value()

    fun position(x: Number, y: Number) {
        this.x = x.asValue()
        this.y = y.asValue()
    }

    companion object : SchemeSpec<Annotation>(::Annotation)
}