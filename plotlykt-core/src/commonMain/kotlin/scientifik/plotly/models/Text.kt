package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.values.asValue
import scientifik.plotly.lazySpec

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

    fun position(x: Number, y: Number) {
        this.x = x.asValue()
        this.y = y.asValue()
    }

    companion object : SchemeSpec<Text>(::Text)
}