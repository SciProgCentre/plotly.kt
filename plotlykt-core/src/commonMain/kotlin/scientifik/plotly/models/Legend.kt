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

enum class TraceOrder {
    normal,
    reversed
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

    /**
     * The order at which the legend items are displayed.
     * "normal": top-to-bottom in the same order as the input data.
     * "reversed": the items are displayed in the opposite order.
     */
    var traceorder by enum(TraceOrder.normal)

    fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    companion object : SchemeSpec<Legend>(::Legend)
}