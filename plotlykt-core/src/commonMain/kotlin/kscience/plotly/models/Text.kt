package kscience.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import kscience.plotly.numberGreaterThan
import kscience.plotly.numberInRange

public enum class HorizontalAlign {
    left,
    right,
    center
}

public enum class VerticalAlign {
    top,
    bottom,
    middle
}

/**
 * Text annotation
 */
public class Text : Scheme() {
    /**
     * Determines whether or not this annotation is visible.
     * Default: true
     */
    public var visible: Boolean? by boolean()

    /**
     * Sets the text associated with this annotation.
     * Plotly uses a subset of HTML tags to do things
     * like newline (<br>), bold (<b></b>), italics (<i></i>),
     * hyperlinks (<a href='...'></a>). Tags <em>, <sup>,
     * <sub> <span> are also supported.
     */
    public var text: String? by string()

    /**
     * Sets the annotation text font.
     */
    public var font: Font by spec(Font)

    /**
     * Sets the annotation's x position. If the axis `type` is "log",
     * then you must take the log of your desired range. If the
     * axis `type` is "date", it should be date strings, like date data,
     * though Date objects and unix milliseconds will be accepted and
     * converted to strings. If the axis `type` is "category",
     * it should be numbers, using the scale where each category
     * is assigned a serial number from zero in the order it appears.
     */
    public var x: Value? by value()

    /**
     * Sets the annotation's y position. If the axis `type` is "log",
     * then you must take the log of your desired range. If the
     * axis `type` is "date", it should be date strings, like date data,
     * though Date objects and unix milliseconds will be accepted and
     * converted to strings. If the axis `type` is "category",
     * it should be numbers, using the scale where each category
     * is assigned a serial number from zero in the order it appears.
     */
    public var y: Value? by value()

    /**
     * Sets an explicit width for the text box. null (default) lets the text set the box width.
     * Wider text will be clipped. There is no automatic wrapping; use <br> to start a new line.
     */
    public var width: Number by numberGreaterThan(1)

    /**
     * Sets an explicit height for the text box. null (default) lets the text set the box height.
     * Taller text will be clipped.
     */
    public var height: Number by numberGreaterThan(1)

    /**
     * Sets the opacity of the annotation (text + arrow).
     */
    public var opacity: Number by numberInRange(0.0..1.0)

    /**
     * Sets the background color of the annotation.
     * Default: "rgba(0, 0, 0, 0)"
     */
    public var bgcolor: Color = Color(this, "bgcolor".asName())

    /**
     * Sets the background color of the annotation.
     * Default: "rgba(0, 0, 0, 0)"
     */
    public var bordercolor: Color = Color(this, "bordercolor".asName())

    /**
     * Determines whether or not the annotation is drawn with an arrow. If "true", `text`
     * is placed near the arrow's tail. If "false", `text` lines up with the `x` and `y` provided.
     */
    public var showarrow: Boolean? by boolean()

    /**
     * Sets the color of the annotation arrow.
     */
    public var arrowcolor: Color = Color(this, "arrowcolor".asName())

    /**
     * Sets the angle at which the `text` is drawn with respect to the horizontal.
     */
    public var textangle: Number by numberInRange(-360.0..360.0)

    /**
     * Sets a distance, in pixels, to move the end arrowhead away from the position
     * it is pointing at, for example to point at the edge of a marker independent of zoom.
     * Note that this shortens the arrow from the `ax` / `ay` vector, in contrast
     * to `xshift` / `yshift` which moves everything by this amount.
     */
    public var standoff: Number by numberGreaterThan(0)

    /**
     * Sets the x component of the arrow tail about the arrow head. If `axref` is `pixel`, a positive (negative)
     * component corresponds to an arrow pointing from right to left (left to right). If `axref` is an axis,
     * this is an absolute value on that axis, like `x`, NOT a relative value.
     */
    public var ax: Value? by value()

    /**
     * Sets the y component of the arrow tail about the arrow head. If `ayref` is `pixel`, a positive (negative)
     * component corresponds to an arrow pointing from bottom to top (top to bottom). If `ayref` is an axis,
     * this is an absolute value on that axis, like `y`, NOT a relative value.
     */
    public var ay: Value? by value()

    /**
     * Sets the annotation's x coordinate axis. If set to an x axis id (e.g. "x" or "x2"), the `x` position refers
     * to an x coordinate If set to "paper", the `x` position refers to the distance from the left side
     * of the plotting area in normalized coordinates where 0 (1) corresponds to the left (right) side.
     */
    public var xref: String? by string()

    /**
     * Sets the annotation's y coordinate axis. If set to an y axis id (e.g. "y" or "y2"), the `y` position refers
     * to an y coordinate If set to "paper", the `y` position refers to the distance from the bottom
     * of the plotting area in normalized coordinates where 0 (1) corresponds to the bottom (top).
     */
    public var yref: String? by string()

    /**
     * Sets the horizontal alignment of the `text` within the box. Has an effect only if `text` spans two or more lines
     * (i.e. `text` contains one or more <br> HTML tags) or if an explicit width is set to override the text width.
     * Default: center.
     */
    public var align: HorizontalAlign by enum(HorizontalAlign.center)

    /**
     * Sets the vertical alignment of the `text` within the box. Has an effect only if an explicit height is set
     * to override the text height. Default: middle.
     */
    public var valign: VerticalAlign by enum(VerticalAlign.middle)

    /**
     * Sets the text box's horizontal position anchor This anchor binds the `x` position to the "left",
     * "center" or "right" of the annotation. For example, if `x` is set to 1, `xref` to "paper" and `xanchor` to
     * "right" then the right-most portion of the annotation lines up with the right-most edge of the plotting area.
     * If "auto", the anchor is equivalent to "center" for data-referenced annotations or if there is an arrow,
     * whereas for paper-referenced with no arrow, the anchor picked corresponds to the closest side.
     * Default: auto.
     */
    public var xanchor: XAnchor by enum(XAnchor.auto)

    /**
     * Sets the text box's vertical position anchor This anchor binds the `y` position to the "top", "middle"
     * or "bottom" of the annotation. For example, if `y` is set to 1, `yref` to "paper" and `yanchor` to "top" then
     * the top-most portion of the annotation lines up with the top-most edge of the plotting area. If "auto",
     * the anchor is equivalent to "middle" for data-referenced annotations or if there is an arrow, whereas
     * for paper-referenced with no arrow, the anchor picked corresponds to the closest side.
     * Default: auto.
     */
    public var yanchor: YAnchor by enum(YAnchor.auto)

    public fun position(x: Number, y: Number) {
        this.x = x.asValue()
        this.y = y.asValue()
    }

    public fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    public companion object : SchemeSpec<Text>(::Text)
}