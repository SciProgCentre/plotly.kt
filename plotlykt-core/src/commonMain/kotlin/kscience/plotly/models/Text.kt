package kscience.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.asValue
import kscience.plotly.lazySpec
import kscience.plotly.numberGreaterThan
import kscience.plotly.numberInRange

enum class HorizontalAlign {
    left,
    right,
    center
}

enum class VerticalAlign {
    top,
    bottom,
    middle
}

/**
 * Text annotation
 */
class Text : Scheme() {
    /**
     * Determines whether or not this annotation is visible.
     * Default: true
     */
    var visible by boolean()

    /**
     * Sets the text associated with this annotation.
     * Plotly uses a subset of HTML tags to do things
     * like newline (<br>), bold (<b></b>), italics (<i></i>),
     * hyperlinks (<a href='...'></a>). Tags <em>, <sup>,
     * <sub> <span> are also supported.
     */
    var text by string()

    /**
     * Sets the annotation text font.
     */
    var font by lazySpec(Font)

    /**
     * Sets the annotation's x position. If the axis `type` is "log",
     * then you must take the log of your desired range. If the
     * axis `type` is "date", it should be date strings, like date data,
     * though Date objects and unix milliseconds will be accepted and
     * converted to strings. If the axis `type` is "category",
     * it should be numbers, using the scale where each category
     * is assigned a serial number from zero in the order it appears.
     */
    var x by value()

    /**
     * Sets the annotation's y position. If the axis `type` is "log",
     * then you must take the log of your desired range. If the
     * axis `type` is "date", it should be date strings, like date data,
     * though Date objects and unix milliseconds will be accepted and
     * converted to strings. If the axis `type` is "category",
     * it should be numbers, using the scale where each category
     * is assigned a serial number from zero in the order it appears.
     */
    var y by value()

    /**
     * Sets an explicit width for the text box. null (default) lets the text set the box width.
     * Wider text will be clipped. There is no automatic wrapping; use <br> to start a new line.
     */
    var width by numberGreaterThan(1)

    /**
     * Sets an explicit height for the text box. null (default) lets the text set the box height.
     * Taller text will be clipped.
     */
    var height by numberGreaterThan(1)

    /**
     * Sets the opacity of the annotation (text + arrow).
     */
    var opacity by numberInRange(0.0..1.0)

    /**
     * Sets the background color of the annotation.
     * Default: "rgba(0, 0, 0, 0)"
     */
    var bgcolor = Color(this, "bgcolor".asName())

    /**
     * Sets the background color of the annotation.
     * Default: "rgba(0, 0, 0, 0)"
     */
    var bordercolor = Color(this, "bordercolor".asName())

    /**
     * Determines whether or not the annotation is drawn with an arrow. If "true", `text`
     * is placed near the arrow's tail. If "false", `text` lines up with the `x` and `y` provided.
     */
    var showarrow by boolean()

    /**
     * Sets the color of the annotation arrow.
     */
    var arrowcolor = Color(this, "arrowcolor".asName())

    /**
     * Sets the angle at which the `text` is drawn with respect to the horizontal.
     */
    var textangle by numberInRange(-360.0..360.0)

    /**
     * Sets a distance, in pixels, to move the end arrowhead away from the position
     * it is pointing at, for example to point at the edge of a marker independent of zoom.
     * Note that this shortens the arrow from the `ax` / `ay` vector, in contrast
     * to `xshift` / `yshift` which moves everything by this amount.
     */
    var standoff by numberGreaterThan(0)

    /**
     * Sets the x component of the arrow tail about the arrow head. If `axref` is `pixel`, a positive (negative)
     * component corresponds to an arrow pointing from right to left (left to right). If `axref` is an axis,
     * this is an absolute value on that axis, like `x`, NOT a relative value.
     */
    var ax by value()

    /**
     * Sets the y component of the arrow tail about the arrow head. If `ayref` is `pixel`, a positive (negative)
     * component corresponds to an arrow pointing from bottom to top (top to bottom). If `ayref` is an axis,
     * this is an absolute value on that axis, like `y`, NOT a relative value.
     */
    var ay by value()

    /**
     * Sets the annotation's x coordinate axis. If set to an x axis id (e.g. "x" or "x2"), the `x` position refers
     * to an x coordinate If set to "paper", the `x` position refers to the distance from the left side
     * of the plotting area in normalized coordinates where 0 (1) corresponds to the left (right) side.
     */
    var xref by string()

    /**
     * Sets the annotation's y coordinate axis. If set to an y axis id (e.g. "y" or "y2"), the `y` position refers
     * to an y coordinate If set to "paper", the `y` position refers to the distance from the bottom
     * of the plotting area in normalized coordinates where 0 (1) corresponds to the bottom (top).
     */
    var yref by string()

    /**
     * Sets the horizontal alignment of the `text` within the box. Has an effect only if `text` spans two or more lines
     * (i.e. `text` contains one or more <br> HTML tags) or if an explicit width is set to override the text width.
     * Default: center.
     */
    var align by enum(HorizontalAlign.center)

    /**
     * Sets the vertical alignment of the `text` within the box. Has an effect only if an explicit height is set
     * to override the text height. Default: middle.
     */
    var valign by enum(VerticalAlign.middle)

    /**
     * Sets the text box's horizontal position anchor This anchor binds the `x` position to the "left",
     * "center" or "right" of the annotation. For example, if `x` is set to 1, `xref` to "paper" and `xanchor` to
     * "right" then the right-most portion of the annotation lines up with the right-most edge of the plotting area.
     * If "auto", the anchor is equivalent to "center" for data-referenced annotations or if there is an arrow,
     * whereas for paper-referenced with no arrow, the anchor picked corresponds to the closest side.
     * Default: auto.
     */
    var xanchor by enum(XAnchor.auto)

    /**
     * Sets the text box's vertical position anchor This anchor binds the `y` position to the "top", "middle"
     * or "bottom" of the annotation. For example, if `y` is set to 1, `yref` to "paper" and `yanchor` to "top" then
     * the top-most portion of the annotation lines up with the top-most edge of the plotting area. If "auto",
     * the anchor is equivalent to "middle" for data-referenced annotations or if there is an arrow, whereas
     * for paper-referenced with no arrow, the anchor picked corresponds to the closest side.
     * Default: auto.
     */
    var yanchor by enum(YAnchor.auto)

    fun position(x: Number, y: Number) {
        this.x = x.asValue()
        this.y = y.asValue()
    }

    fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    companion object : SchemeSpec<Text>(::Text)
}