package kscience.plotly.models

import hep.dataforge.meta.*
import kscience.plotly.numberGreaterThan

class Contours : Scheme() {
    /**
     * If `levels`, the data is represented as a contour plot with multiple
     * levels displayed. If `constraint`, the data is represented as constraints
     * with the invalid region shaded as specified by the `operation` and `value` parameters.
     */
    var type by enum(ContoursType.levels)

    /**
     * Sets the starting contour level value. Must be less than `contours.end`
     */
    var start by number()

    /**
     * Sets the end contour level value. Must be more than `contours.start`
     */
    var end by number()

    /**
     * Sets the step between each contour level. Must be positive.
     */
    var size by numberGreaterThan(0)

    /**
     * Determines the coloring method showing the contour values.
     * If "fill" (default), coloring is done evenly between each contour level
     * If "heatmap", a heatmap gradient coloring is applied between each contour level.
     * If "lines", coloring is done on the contour lines. If "none",
     * no coloring is applied on this trace.
     */
    var coloring by enum(ContoursColoring.fill)

    /**
     * Determines whether or not the contour lines are drawn.
     * Has an effect only if `contours.coloring` is set to "fill".
     */
    var showlines by boolean()

    /**
     * Determines whether to label the contour lines with their values.
     */
    var showlabels by boolean()

    /**
     * Sets the font used for labeling the contour levels. The default color
     * comes from the lines, if shown. The default family and size come from `layout.font`.
     */
    var labelfont by spec(Font)

    fun labelfont(block: Font.() -> Unit) {
        labelfont = Font(block)
    }

    companion object : SchemeSpec<Contours>(::Contours)
}

interface ContourSpec {
    var ncontours: Int

    var contours: Contours?

    var autocontour: Boolean?
}