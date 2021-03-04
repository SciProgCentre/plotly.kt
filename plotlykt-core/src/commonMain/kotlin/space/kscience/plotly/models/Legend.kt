package space.kscience.plotly.models

import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.dataforge.meta.enum
import space.kscience.dataforge.meta.spec
import space.kscience.plotly.numberGreaterThan
import space.kscience.plotly.numberInRange
import kotlin.js.JsName

public enum class LegendOrientation {
    @JsName("v")
    vertical,

    @JsName("h")
    horizontal
}

public enum class XAnchor {
    auto,
    left,
    center,
    right
}

public enum class YAnchor {
    auto,
    top,
    middle,
    bottom
}

public enum class TraceOrder {
    normal,
    reversed
}

public class Legend : Scheme() {
    /**
     * Sets the legend background color. Defaults to `paper_bgcolor`.
     */
    public val bgcolor: Color by color()

    /**
     * Sets the color of the border enclosing the legend.
     * Default: #444.
     */
    public val bordercolor: Color by color()

    /**
     * Sets the width (in px) of the border enclosing the legend.
     * Default: 0.
     */
    public var borderwidth: Number by numberGreaterThan(0)

    /**
     * Number between or equal -2 and 3.
     * Sets the x position (in normalized coordinates) of the legend.
     * Defaults to "1.02" for vertical legends and
     * defaults to "0" for horizontal legends.
     */
    public var x: Number by numberInRange(-2.0..3.0)

    /**
     * Sets the legend's horizontal position anchor.
     * This anchor binds the `x` position to the "left",
     * "center" or "right" of the legend. Value "auto"
     * anchors legends to the right for `x` values
     * greater than or equal to 2/3, anchors legends
     * to the left for `x` values less than or equal to 1/3
     * and anchors legends with respect to their center otherwise.
     * Default: left.
     */
    public var xanchor: XAnchor by enum(XAnchor.left)

    /**
     * Number between or equal to -2 and 3.
     * Sets the y position (in normalized coordinates) of the legend.
     * Defaults to "1" for vertical legends, defaults to "-0.1"
     * for horizontal legends on graphs w/o range sliders and
     * defaults to "1.1" for horizontal legends on graph
     * with one or multiple range sliders.
     */
    public var y: Number by numberInRange(-2.0..3.0)

    /**
     * Sets the legend's vertical position anchor.
     * This anchor binds the `y` position to the "top", "middle"
     * or "bottom" of the legend. Value "auto" anchors legends
     * at their bottom for `y` values less than or equal to 1/3,
     * anchors legends to at their top for `y` values
     * greater than or equal to 2/3 and anchors legends
     * with respect to their middle otherwise.
     */
    public var yanchor: YAnchor by enum(YAnchor.auto)

    /**
     * Sets the font used to text the legend items.
     */
    public var font: Font by spec(Font)

    /**
     * Sets the orientation of the legend (vertical/horizontal).
     * Default: vertical.
     */
    public var orientation: LegendOrientation by enum(LegendOrientation.vertical)

    /**
     * The order at which the legend items are displayed.
     * "normal": top-to-bottom in the same order as the input data.
     * "reversed": the items are displayed in the opposite order.
     */
    public var traceorder: TraceOrder by enum(TraceOrder.normal)

    public fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    public companion object : SchemeSpec<Legend>(::Legend)
}