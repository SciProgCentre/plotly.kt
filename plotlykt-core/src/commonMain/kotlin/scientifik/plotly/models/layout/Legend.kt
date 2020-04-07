package scientifik.plotly.models.layout

import hep.dataforge.meta.*
import scientifik.plotly.models.Font
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
    var borderwidth by int(0)
    var x by double(1.02) //FIXME("number between or equal to -2 and 3")
    var xanchor by enum(XAnchor.left)
    var y by double(1.0)//FIXME("number between or equal to -2 and 3")
    var yanchor by enum(YAnchor.auto)
    var font by spec(Font)
    var orientation by enum(Orientation.vertical)

    // var traceorder
    fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    companion object : SchemeSpec<Legend>(::Legend)
}