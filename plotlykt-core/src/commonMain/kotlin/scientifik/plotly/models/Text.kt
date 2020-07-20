package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.asValue
import scientifik.plotly.lazySpec
import scientifik.plotly.numberGreaterThan
import scientifik.plotly.numberInRange

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
    var text by string("")

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

    fun position(x: Number, y: Number) {
        this.x = x.asValue()
        this.y = y.asValue()
    }

    companion object : SchemeSpec<Text>(::Text)
}