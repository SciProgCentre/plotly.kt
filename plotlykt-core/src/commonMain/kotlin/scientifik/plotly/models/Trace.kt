package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import scientifik.plotly.*
import kotlin.js.JsName


enum class TraceMode {
    lines,

    @JsName("linesMarkers")
    `lines+markers`,
    markers
}

enum class TraceType {
    // Simple
    scatter,
    scattergl,
    bar,
    pie,
    heatmap,
    contour,

    @UnsupportedPlotlyAPI
    table,
    @UnsupportedPlotlyAPI
    image,

    // Distributions
    box,
    violin,
    histogram,
    histogram2d,
    histogram2dcontour,
//    // Finance
//    ohlc,
//    candlestick,
//    waterfall,
//    // 3D
//    scatter3d,
//    surface,
//    mesh3d,
//    cone,
//    streamtube,
//    volume,
//    isosurface,
//    // Maps
//    scattergeo,
//    choropleth,
//    scattermapbox
//    // Specialized
}

enum class Visible {
    @JsName("true")
    True,

    @JsName("false")
    False,
    legendonly
}

enum class Symbol {
    circle,

    @JsName("triangleUp")
    `triangle-up`,

    @JsName("triangleDown")
    `triangle-down`,

    @JsName("squareCross")
    `square-cross`,

    @JsName("crossThin")
    `cross-thin`,
    cross,

    @JsName("lineNs")
    `line-ns`
}

enum class SizeMode {
    diameter,
    area
}

class MarkerLine : Line() {
    /**
     * Number than or equal to 0. Sets the width (in px)
     * of the lines bounding the marker points.
     */
    var width by intGreaterThan(0)

    /**
     * Sets themarker.linecolor. It accepts either a specific color
     * or an array of numbers that are mapped to the colorscale
     * relative to the max and min values of the array or relative to
     * `cmin` and `cmax` if set.
     */
    val color = Color(this, "color".asName())

    /**
     * Determines whether or not the color domain is computed with respect
     * to the input data (here in `color`) or the bounds set in `cmin` and `cmax`
     * Has an effect only if in `color` is set to a numerical array.
     * Defaults to `false` when `cmin` and `cmax` are set by the user.
     *
     */
    var cauto by boolean()

    /**
     * Sets the lower bound of the color domain. Has an effect only if
     * in `color`is set to a numerical array. Value should have
     * the same units as in `color` and if set, `cmax` must be set as well.
     */
    var cmin by int()

    /**
     * Sets the upper bound of the color domain. Has an effect only if
     * in `color`is set to a numerical array. Value should have
     * the same units as in `color` and if set, `cmin` must be set as well.
     */
    var cmax by int()

    /**
     * Sets the mid-point of the color domain by scaling `cmin` and/or `cmax`
     * to be equidistant to this point. Has an effect only if in `color`
     * is set to a numerical array. Value should have the same units
     * as in `color`. Has no effect when `cauto` is `false`.
     */
    var cmid by int()

    /**
     * Sets the colorscale. Has an effect only if in `color`is set to a numerical array.
     * The colorscale must be an array containing arrays mapping a normalized value
     * to an rgb, rgba, hex, hsl, hsv, or named color string. At minimum,
     * a mapping for the lowest (0) and highest (1) values are required. For example,
     * `[[0, 'rgb(0,0,255)'], [1, 'rgb(255,0,0)']]`. To control the bounds of the colorscale
     * in color space, use `cmin` and `cmax`. Alternatively, `colorscale` may be
     * a palette name string of the following list: Greys, YlGnBu, Greens, YlOrRd,
     * Bluered, RdBu, Reds, Blues, Picnic, Rainbow, Portland, Jet, Hot, Blackbody,
     * Earth, Electric, Viridis, Cividis.
     */
    var colorscale by value() // TODO()

    /**
     * Determines whether the colorscale is a default palette (`true`) or
     * the palette determined by `colorscale`. Has an effect only if
     * in `color` is set to a numerical array. In case `colorscale`
     * is unspecified or `autocolorscale` is true, the default palette
     * will be chosen according to whether numbers in the `color` array
     * are all positive, all negative or mixed.
     * Default: true.
     */
    var autocolorscale by boolean() // TODO("colorscale first!")

    /**
     * Reverses the color mapping if true. Has an effect only if
     * in `color` is set to a numerical array. If true, `cmin`
     * will correspond to the last color in the array and `cmax`
     * will correspond to the first color.
     */
    var reversescale by boolean() // TODO("colorscale first!")

    /**
     * Sets a reference to a shared color axis. References to these
     * shared color axes are "coloraxis", "coloraxis2", "coloraxis3", etc.
     * Settings for these shared color axes are set in the layout,
     * under `coloraxis`, `coloraxis2`, etc. Note that multiple
     * color scales can be linked to the same color axis.
     */
    // var coloraxis TODO()

    companion object : SchemeSpec<MarkerLine>(::MarkerLine)
}

enum class TextPosition {
    topLeft,
    topCenter,
    topRight,
    middleLeft,
    middleCenter,
    middleRight,
    bottomLeft,
    bottomCenter,
    bottomRight,
}

class Font : Scheme() {
    /**
     * HTML font family - the typeface that will be applied
     * by the web browser. The web browser will only be able
     * to apply a font if it is available on the system
     * which it operates. Provide multiple font families,
     * separated by commas, to indicate the preference
     * in which to apply fonts if they aren't available
     * on the system. The Chart Studio Cloud (at https://chart-studio.plotly.com or on-premise)
     * generates images on a server, where only a select number
     * of fonts are installed and supported. These include "Arial",
     * "Balto", "Courier New", "Droid Sans",, "Droid Serif",
     * "Droid Sans Mono", "Gravitas One", "Old Standard TT",
     * "Open Sans", "Overpass", "PT Sans Narrow", "Raleway", "Times New Roman".
     */
    var family by string()

    var size by intGreaterThan(1)

    val color = Color(this, "color".asName())

    companion object : SchemeSpec<Font>(::Font)
}


class Title : Scheme() {
    /**
     * Sets the plot's title.
     */
    var text by string()

    /**
     * Sets the title font.
     */
    var font by spec(Font)

    fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    companion object : SchemeSpec<Title>(::Title)
}

enum class ErrorType {
    percent,
    constant,
    sqrt,
    data
}

class Error : Scheme() {
    /**
     * Determines whether or not this set of error bars is visible.
     */
    var visible by boolean()

    /**
     * Enumerated , one of ("percent" | "constant" | "sqrt" | "data")
     * Determines the rule used to generate the error bars.
     * If "constant`, the bar lengths are of a constant value.
     * Set this constant in `value`. If "percent", the bar lengths correspond
     * to a percentage of underlying data. Set this percentage in `value`.
     * If "sqrt", the bar lengths correspond to the square of the underlying data.
     * If "data", the bar lengths are set with data set `array`.
     * Default: constant.
     */
    var type by enum(ErrorType.constant)

    /**
     * Determines whether or not the error bars have the same length in both
     * direction (top/bottom for vertical bars, left/right for horizontal bars.
     */
    var symmetric by boolean()

    /**
     * Sets the data corresponding the length of each error bar.
     * Values are plotted relative to the underlying data.
     */
    var array by numberList()

    /**
     * Sets the data corresponding the length of each error bar
     * in the bottom (left) direction for vertical (horizontal) bars.
     * Values are plotted relative to the underlying data.
     */
    var arrayminus by numberList()

    /**
     * Number greater than or equal to 0.
     * Sets the value of either the percentage (if `type` is set to "percent")
     * or the constant (if `type` is set to "constant")
     * corresponding to the lengths of the error bars.
     * Default: 10.
     */
    var value by doubleGreaterThan(0.0)

    /**
     * Number greater than or equal to 0.
     * Sets the value of either the percentage (if `type` is set to "percent")
     * or the constant (if `type` is set to "constant")
     * corresponding to the lengths of the error bars in the bottom (left)
     * direction for vertical (horizontal) bars.
     * Default: 10.
     */
    var valueminus by doubleGreaterThan(0.0)

    /**
     * Sets the stoke color of the error bars.
     */
    var color = Color(this, "color".asName())

    /**
     * Sets the thickness (in px) of the error bars.
     * Default: 2.
     */
    var thickness by intGreaterThan(0)

    /**
     * Sets the width (in px) of the cross-bar at both ends of the error bars.
     */
    var width by intGreaterThan(0)

    /**
     * Integer greater than or equal to 0.
     * Default: 0.
     */
    var traceref by intGreaterThan(0)

    /**
     * Integer greater than or equal to 0.
     * Default: 0.
     */
    var tracerefminus by intGreaterThan(0)

    companion object : SchemeSpec<Error>(::Error)
}

enum class MeasureMode {
    fraction,
    pixels
}

class ColorBar : Scheme() {
    /**
     * Determines whether this color bar's thickness (i.e. the measure
     * in the constant color direction) is set in units of plot "fraction"
     * or in "pixels" (default). Use `thickness` to set the value.
     */
    var thicknessmode by enum(MeasureMode.pixels)

    /**
     * Sets the thickness of the color bar.
     * This measure excludes the size of the padding, ticks and labels.
     * Default: 30.
     */
    var thickness by doubleGreaterThan(0.0)

    /**
     * Determines whether this color bar's length (i.e. the measure
     * in the color variation direction) is set in units of plot
     * "fraction" (default) or in "pixels. Use `len` to set the value.
     */
    var lenmode by enum(MeasureMode.fraction)

    /**
     * Sets the length of the color bar This measure excludes
     * the padding of both ends. That is, the color bar length
     * is this length minus the padding on both ends.
     * Default: 1.
     */
    var len by doubleGreaterThan(0.0)

    /**
     * Sets the x position of the color bar (in plot fraction).
     * Default: 1.02.
     */
    var x by doubleInRange(-2.0..3.0)

    /**
     * Sets this color bar's horizontal position anchor.
     * This anchor binds the `x` position to the "left" (default),
     * "center" or "right" of the color bar.
     */
    var xanchor by enum(XAnchor.left)

    /**
     * Sets the amount of padding (in px) along the x direction.
     * Default: 10.
     */
    var xpad by intGreaterThan(0)

    /**
     * Sets the y position of the color bar (in plot fraction).
     * Default: 0.5.
     */
    var y by doubleInRange(-2.0..3.0)

    /**
     * Sets this color bar's vertical position anchor.
     * This anchor binds the `y` position to the "top",
     * "middle" (default) or "bottom" of the color bar.
     */
    var yanchor by enum(YAnchor.middle)

    /**
     * Sets the amount of padding (in px) along the y direction.
     * Default: 10.
     */
    var ypad by intGreaterThan(0)

    /**
     * Sets the axis line color.
     * Default: #444.
     */
    var bordercolor = Color(this, "bordercolor".asName())

    /**
     * Sets the width (in px) or the border enclosing this color bar.
     * Default: 0.
     */
    var borderwidth by intGreaterThan(0)

    /**
     * Sets the color of padded area.
     * Default: rgba(0, 0, 0, 0).
     */
    var bgcolor = Color(this, "bgcolor".asName())

    var title by spec(Title)

    /**
     * Sets the width (in px) of the axis line.
     * Default: 1.
     */
    var outlinewidth by intGreaterThan(0)

    /**
     * Sets the axis line color.
     * Default: #444.
     */
    var outlinecolor = Color(this, "outlinecolor".asName())

    fun title(block: Title.() -> Unit) {
        title = Title(block)
    }

    companion object : SchemeSpec<ColorBar>(::ColorBar)
}

enum class Orientation {
    @JsName("v")
    vertical,

    @JsName("h")
    horizontal
}

enum class ViolinHoveron {
    violins,
    points,
    kde,
    `violins+points`,
    `violins+kde`,
    `points+kde`,
    `violins+points+kde`,
    all
}

class MeanLine : Scheme() {
    /**
     * Sets the mean line width.
     */
    var width by intGreaterThan(0)

    /**
     * Sets the mean line color.
     */
    var color = Color(this, "color".asName())

    /**
     * Determines if a line corresponding to the sample's
     * mean is shown inside the violins. If `box.visible` is
     * turned on, the mean line is drawn inside the inner box.
     * Otherwise, the mean line is drawn from one side of the violin to other.
     */
    var visible by boolean()

    companion object : SchemeSpec<MeanLine>(::MeanLine)
}

enum class ViolinPoints {
    all,
    outliers,
    suspectedoutliers,
    `false`
}

enum class ViolinSide {
    positive,
    negative,
    both
}

class Box : Scheme() {
    /**
     * Sets the width of the inner box plots relative to the violins' width.
     * For example, with 1, the inner box plots are as wide as the violins.
     * Default: 0.25.
     */
    var width by doubleInRange(0.0..1.0)

    /**
     * Determines if an miniature box plot is drawn inside the violins.
     */
    var visible by boolean()

    /**
     * Sets the inner box plot fill color.
     */
    var fillcolor = Color(this, "fillcolor".asName())

    companion object : SchemeSpec<Box>(::Box)
}

enum class ViolinScaleMode {
    count,
    width
}

enum class ContoursType {
    levels,
    constraint
}

enum class ContoursColoring {
    fill,
    heatmap,
    lines,
    none
}


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
    var start by int()

    /**
     * Sets the end contour level value. Must be more than `contours.start`
     */
    var end by int()

    /**
     * Sets the step between each contour level. Must be positive.
     */
    var size by intGreaterThan(0)

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
    var showlables by boolean()

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

open class Trace() : Scheme() {
    fun axis(axisName: String) = TraceValues(this, axisName)

    /**
     * Sets the x coordinates.
     */
    val x = axis(X_AXIS)

    /**
     * Sets the y coordinates.
     */
    val y = axis(Y_AXIS)

    /**
     * Sets the y coordinate for single-box traces or the starting coordinate
     * for multi-box traces set using q1/median/q3. See overview for more info.
     */
    var y0 by value()

    /**
     * Data array. Sets the z data.
     */
    var z by list()

    /**
     * Data array. Sets the values of the sectors.
     * If omitted, we count occurrences of each label.
     */
    var values by list()

    /**
     * Data array. Sets the sector labels. If `labels` entries
     * are duplicated, we sum associated `values` or simply
     * count occurrences if `values` is not provided.
     * For other array attributes (including color) we use the first
     * non-empty entry among all occurrences of the label.
     */
    var labels by list()

    /**
     * Sets the colorscale. The colorscale must be an array
     * containing arrays mapping a normalized value to an rgb,
     * rgba, hex, hsl, hsv, or named color string.
     */
    var colorscale by value()

    var colorbar by spec(ColorBar)

    /**
     * Sets the trace name. The trace name appear as the legend item and on hover.
     */
    var name by string()

    /**
     * Flaglist string. Any combination of "lines", "markers", "text"
     * joined with a "+" OR "none". Determines the drawing mode for
     * this scatter trace. If the provided `mode` includes "text" then
     * the `text` elements appear at the coordinates. Otherwise,
     * the `text` elements appear on hover.
     * Default: lines.
     */
    var mode by enum(TraceMode.lines)

    var type by enum(TraceType.scatter)

    /**
     * Enumerated , one of ( true | false | "legendonly" )
     * Determines whether or not this trace is visible.
     * If "legendonly", the trace is not drawn, but can appear
     * as a legend item (provided that the legend itself is visible).
     * Default: true.
     */
    var visible by enum(Visible.True)

    /**
     * Determines whether or not an item corresponding to this trace is shown in the legend.
     * Default: true
     */
    var showlegend by boolean()

    /**
     * Sets the legend group for this trace. Traces part of
     * the same legend group hide/show at the same time when toggling legend items.
     * Default: ""
     */
    var legendgroup by string()

    /**
     * Sets the opacity of the trace.
     * Default: 1.
     */
    var opacity by doubleInRange(0.0..1.0)

    //var line by spec(Line)

    var marker by spec(Marker)

    /**
     * Sets text elements associated with each (x,y) pair.
     * If a single string, the same string appears over
     * all the data points. If an array of string, the items
     * are mapped in order to the this trace's (x,y) coordinates.
     * If trace `hoverinfo` contains a "text" flag and "hovertext" is not set,
     * these elements will be seen in the hover labels.
     */
    var text by stringList()

    /**
     * Sets the positions of the `text` elements
     * with respects to the (x,y) coordinates.
     * Default: "middle center".
     */
    var textposition by enum(TextPosition.middleCenter)

    /**
     * Sets the text font.
     */
    var textfont by spec(Font)

    var error_x by spec(Error)

    var error_y by spec(Error)

    /**
     * Sets the orientation of the plot(s).
     * If "vertical" ("horizontal"), the data
     * is visualized along the vertical (horizontal).
     */
    var orientation by enum(Orientation.horizontal)

    /**
     * Do the hover effects highlight individual violins or sample
     * points or the kernel density estimate or any combination of them?
     */
    var hoveron by enum(ViolinHoveron.`violins+points+kde`)

    var meanline by spec(MeanLine)

    /**
     * If there are multiple violins that should be sized according to
     * some metric (see `scalemode`), link them by providing a non-empty
     * group id here shared by every trace in the same group.
     * If a violin's `width` is undefined, `scalegroup` will default
     * to the trace's name. In this case, violins with the same names
     * will be linked together. Default: ""
     */
    var scalegroup by string()

    /**
     * If "outliers", only the sample points lying outside the whiskers
     * are shown If "suspectedoutliers", the outlier points are shown
     * and points either less than 4"Q1-3"Q3 or greater than 4"Q3-3"Q1
     * are highlighted (see `outliercolor`) If "all", all sample points
     * are shown If "false", only the violins are shown with no sample points.
     */
    var points by enum(ViolinPoints.`false`)

    /**
     * Sets the position of the sample points in relation to the violins.
     * If "0", the sample points are places over the center of the violins.
     * Positive (negative) values correspond to positions to the right (left)
     * for vertical violins and above (below) for horizontal violins.
     */
    var pointpos by doubleInRange(-2.0..2.0)

    /**
     * Sets the amount of jitter in the sample points drawn.
     * If "0", the sample points align along the distribution axis.
     * If "1", the sample points are drawn in a random jitter
     * of width equal to the width of the violins.
     */
    var jitter by doubleInRange(0.0..1.0)

    var box by spec(Box)

    /**
     * Sets the metric by which the width of each violin is determined.
     * "width" (default) means each violin has the same (max) width
     * "count" means the violins are scaled by the number
     * of sample points making up each violin.
     */
    var scalemode by enum(ViolinScaleMode.width)

    /**
     * Determines on which side of the position value the density
     * function making up one half of a violin is plotted.
     * Useful when comparing two violin traces under "overlay" mode,
     * where one trace has `side` set to "positive" and the other to "negative".
     */
    var side by enum(ViolinSide.both)

    var contours by spec(Contours)

    /**
     * Sets the maximum number of contour levels. The actual number of contours
     * will be chosen automatically to be less than or equal to the value of `ncontours`.
     * Has an effect only if `autocontour` is "true" or if `contours.size` is missing.
     * Default: 15.
     */
    var ncontours by intGreaterThan(1)

    /**
     * Determines whether or not the contour level attributes are picked by an algorithm.
     * If "true" (default), the number of contour levels can be set in `ncontours`.
     * If "false", set the contour level attributes in `contours`.
     */
    var autocontour by boolean()

    fun contours(block: Contours.() -> Unit) {
        contours = Contours(block)
    }

    fun z(array: Iterable<Any>) {
        z = array.map{ Value.of(it) }
    }

    fun values(array: Iterable<Any>) {
        values = array.map{ Value.of(it) }
    }

    fun labels(array: Iterable<Any>) {
        labels = array.map{ Value.of(it) }
    }

    fun colorbar(block: ColorBar.() -> Unit) {
        colorbar = ColorBar(block)
    }

    fun meanline(block: MeanLine.() -> Unit) {
        meanline = MeanLine(block)
    }

    fun box(block: Box.() -> Unit) {
        box = Box(block)
    }

    fun textfont(block: Font.() -> Unit) {
        textfont = Font(block)
    }

    fun marker(block: Marker.() -> Unit) {
        marker = Marker(block)
    }

    fun error_x(block: Error.() -> Unit) {
        error_x = Error(block)
    }

    fun error_y(block: Error.() -> Unit) {
        error_y = Error(block)
    }

    companion object : SchemeSpec<Trace>(::Trace) {
        const val X_AXIS = "x"
        const val Y_AXIS = "y"
        const val TEXT_AXIS = "text"

        operator fun invoke(xs: Any, ys: Any? = null/*, zs: Any? = null*/, block: Trace.() -> Unit = {}) = invoke {
            block()
            x.set(xs)
            y.set(ys)
        }
    }
}