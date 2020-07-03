package scientifik.plotly.models

import hep.dataforge.meta.*
import scientifik.plotly.doubleInRange
import scientifik.plotly.intGreaterThan
import kotlin.js.JsName

enum class Orientation {
    @JsName("V")
    vertical,

    @JsName("h")
    horizontal
}

enum class XAnchor {
    auto,
    left,
    center,
    rigth
}

enum class YAnchor {
    auto,
    top,
    middle,
    bottom
}

class Legend : Scheme() {
    //    var bgcolor
//    var bordercolor
    var borderwidth by intGreaterThan(0)
    var x by doubleInRange(-2.0..3.0)
    var xanchor by enum(XAnchor.left)
    var y by doubleInRange(-2.0..3.0)
    var yanchor by enum(YAnchor.auto)
    var font by spec(Font)
    var orientation by enum(Orientation.vertical)

    // var traceorder
    fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    companion object : SchemeSpec<Legend>(::Legend)
}