@file:Suppress("PropertyName", "FunctionName")

package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.asValue
import space.kscience.plotly.*
import kotlin.js.JsName
import kotlin.properties.ReadOnlyProperty


public enum class TraceType {
    // Simple
    scatter,
    scattergl,
    bar,
    pie,
    heatmap,
    heatmapgl,
    contour,

    table,

    @UnsupportedPlotlyAPI
    image,

    // Distributions
    box,
    violin,
    histogram,
    histogram2d,
    histogram2dcontour,

    // Finance
    @UnsupportedPlotlyAPI
    ohlc,
    candlestick,

    @UnsupportedPlotlyAPI
    waterfall,

    // 3D
    @UnsupportedPlotlyAPI
    scatter3d,

    @UnsupportedPlotlyAPI
    surface,

    @UnsupportedPlotlyAPI
    mesh3d,

    @UnsupportedPlotlyAPI
    cone,

    @UnsupportedPlotlyAPI
    streamtube,

    @UnsupportedPlotlyAPI
    volume,

    @UnsupportedPlotlyAPI
    isosurface,

    // Maps
    scattergeo,
    choropleth,
    scattermapbox,
    choroplethmapbox
    // Specialized
}

public enum class Visible {
    `true`,
    `false`,
    legendonly
}

public enum class Symbol {
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

public enum class SizeMode {
    diameter,
    area
}

public class MarkerLine : Scheme(), Line {
    /**
     * Number greater than or equal to 0. Sets the width (in px)
     * of the lines bounding the marker points.
     */
    override var width: Number by numberGreaterThan(0)

    /**
     * Array of numbers greater than or equal to 0. Sets the width (in px)
     * of the lines bounding the marker points.
     */
    override var widthList: List<Number> by numberList(key = "width".asName())

    /**
     * Sets themarker.linecolor. It accepts either a specific color
     * or an array of numbers that are mapped to the colorscale
     * relative to the max and min values of the array or relative to
     * `cmin` and `cmax` if set.
     */
    override val color: Color by color()

    /**
     * Determines whether or not the color domain is computed with respect
     * to the input data (here in `color`) or the bounds set in `cmin` and `cmax`
     * Has an effect only if in `color` is set to a numerical array.
     * Defaults to `false` when `cmin` and `cmax` are set by the user.
     *
     */
    public var cauto: Boolean? by boolean()

    /**
     * Sets the lower bound of the color domain. Has an effect only if
     * in `color`is set to a numerical array. Value should have
     * the same units as in `color` and if set, `cmax` must be set as well.
     */
    public var cmin: Number? by number()

    /**
     * Sets the upper bound of the color domain. Has an effect only if
     * in `color`is set to a numerical array. Value should have
     * the same units as in `color` and if set, `cmin` must be set as well.
     */
    public var cmax: Number? by number()

    /**
     * Sets the mid-point of the color domain by scaling `cmin` and/or `cmax`
     * to be equidistant to this point. Has an effect only if in `color`
     * is set to a numerical array. Value should have the same units
     * as in `color`. Has no effect when `cauto` is `false`.
     */
    public var cmid: Number? by number()

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
    public var colorscale: Value? by value() // TODO()

    /**
     * Determines whether the colorscale is a default palette (`true`) or
     * the palette determined by `colorscale`. Has an effect only if
     * in `color` is set to a numerical array. In case `colorscale`
     * is unspecified or `autocolorscale` is true, the default palette
     * will be chosen according to whether numbers in the `color` array
     * are all positive, all negative or mixed.
     * Default: true.
     */
    public var autocolorscale: Boolean? by boolean() // TODO("colorscale first!")

    /**
     * Reverses the color mapping if true. Has an effect only if
     * in `color` is set to a numerical array. If true, `cmin`
     * will correspond to the last color in the array and `cmax`
     * will correspond to the first color.
     */
    public var reversescale: Boolean? by boolean() // TODO("colorscale first!")

    /**
     * Sets a reference to a shared color axis. References to these
     * shared color axes are "coloraxis", "coloraxis2", "coloraxis3", etc.
     * Settings for these shared color axes are set in the layout,
     * under `coloraxis`, `coloraxis2`, etc. Note that multiple
     * color scales can be linked to the same color axis.
     */
    // var coloraxis TODO()

    /**
     * Sets the border line color of the outlier sample points. Defaults to marker.color
     */
    public val outliercolor: Color by color()

    /**
     * Sets the border line width (in px) of the outlier sample points.
     * Default: 1
     */
    public var outlierwidth: Number by numberGreaterThan(0)

    public fun colors(colors: Iterable<Any>) {
        color.value = colors.map { Value.of(it) }.asValue()
    }

    public companion object : SchemeSpec<MarkerLine>(::MarkerLine)
}

public enum class TextPosition {
    @JsName("topLeft")
    `top left`,

    @JsName("topCenter")
    `top center`,

    @JsName("topRight")
    `top right`,

    @JsName("middleLeft")
    `middle left`,

    @JsName("middleCenter")
    `middle center`,

    @JsName("middleRight")
    `middle right`,

    @JsName("bottomLeft")
    `bottom left`,

    @JsName("bottomCenter")
    `bottom center`,

    @JsName("bottomRight")
    `bottom right`,

    // pie
    inside,
    outside,
    auto,
    none
}

public class Font : Scheme() {
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
    public var family: String? by string()

    /**
     * HTML font family
     */
    public var familiesList: List<String>? by stringList(key = "family".asName())

    public var size: Number by numberGreaterThan(1)

    public var sizesList: List<Number> by numberList(key = "size".asName())

    public val color: Color by color()

    public fun colors(array: Iterable<Any>) {
        color.value = array.map { Value.of(it) }.asValue()
    }

    public companion object : SchemeSpec<Font>(::Font)
}

public enum class Ref {
    container,
    paper
}

public class Title : Scheme() {
    /**
     * Sets the plot's title.
     */
    public var text: String? by string()

    /**
     * Sets the title font.
     */
    public var font: Font by spec(Font)

    /**
     * Sets the container `x` refers to. "container" spans the entire `width` of the plot.
     * "paper" refers to the width of the plotting area only. Default: container.
     */
    public var xref: Ref by enum(Ref.container)

    /**
     * Sets the container `y` refers to. "container" spans the entire `height` of the plot.
     * "paper" refers to the height of the plotting area only. Default: container.
     */
    public var yref: Ref by enum(Ref.container)

    /**
     * Sets the x position with respect to `xref` in normalized coordinates from "0" (left) to "1" (right).
     * Default: 0.5
     */
    public var x: Number by numberInRange(0.0..1.0)

    /**
     * Sets the y position with respect to `yref` in normalized coordinates from "0" (bottom) to "1" (top).
     * "auto" places the baseline of the title onto the vertical center of the top margin. Default: auto.
     */
    public var y: Number by numberInRange(0.0..1.0)

    /**
     * Sets the title's horizontal alignment with respect to its x position. "left" means that the title starts at x,
     * "right" means that the title ends at x and "center" means that the title's center is at x.
     * "auto" divides `xref` by three and calculates the `xanchor` value automatically based on the value of `x`.
     */
    public var xanchor: XAnchor by enum(XAnchor.auto)

    /**
     * Sets the title's vertical alignment with respect to its y position. "top" means that the title's cap line is at y,
     * "bottom" means that the title's baseline is at y and "middle" means that the title's midline is at y.
     * "auto" divides `yref` by three and calculates the `yanchor` value automatically based on the value of `y`.
     */
    public var yanchor: YAnchor by enum(YAnchor.auto)

    /**
     * Sets the padding of the title. Each padding value only applies when the corresponding `xanchor`/`yanchor`
     * value is set accordingly. E.g. for left padding to take effect, `xanchor` must be set to "left".
     * The same rule applies if `xanchor`/`yanchor` is determined automatically. Padding is muted if
     * the respective anchor value is "middle"/"center".
     */
    public var pad: Margin by spec(Margin)

    public fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    public fun pad(block: Margin.() -> Unit) {
        pad = Margin(block)
    }

    public companion object : SchemeSpec<Title>(::Title)
}

public enum class ErrorType {
    percent,
    constant,
    sqrt,
    data
}

public class Error : Scheme() {
    /**
     * Determines whether or not this set of error bars is visible.
     */
    public var visible: Boolean? by boolean()

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
    public var type: ErrorType by enum(ErrorType.constant)

    /**
     * Determines whether or not the error bars have the same length in both
     * direction (top/bottom for vertical bars, left/right for horizontal bars.
     */
    public var symmetric: Boolean? by boolean()

    /**
     * Sets the data corresponding the length of each error bar.
     * Values are plotted relative to the underlying data.
     */
    public var array: List<Number> by numberList()

    /**
     * Sets the data corresponding the length of each error bar
     * in the bottom (left) direction for vertical (horizontal) bars.
     * Values are plotted relative to the underlying data.
     */
    public var arrayminus: List<Number> by numberList()

    /**
     * Number greater than or equal to 0.
     * Sets the value of either the percentage (if `type` is set to "percent")
     * or the constant (if `type` is set to "constant")
     * corresponding to the lengths of the error bars.
     * Default: 10.
     */
    public var value: Number by numberGreaterThan(0)

    /**
     * Number greater than or equal to 0.
     * Sets the value of either the percentage (if `type` is set to "percent")
     * or the constant (if `type` is set to "constant")
     * corresponding to the lengths of the error bars in the bottom (left)
     * direction for vertical (horizontal) bars.
     * Default: 10.
     */
    public var valueminus: Number by numberGreaterThan(0)

    /**
     * Sets the stoke color of the error bars.
     */
    public val color: Color by color()

    /**
     * Sets the thickness (in px) of the error bars.
     * Default: 2.
     */
    public var thickness: Number by numberGreaterThan(0)

    /**
     * Sets the width (in px) of the cross-bar at both ends of the error bars.
     */
    public var width: Number by numberGreaterThan(0)

    /**
     * Integer greater than or equal to 0.
     * Default: 0.
     */
    public var traceref: Int by intGreaterThan(0)

    /**
     * Integer greater than or equal to 0.
     * Default: 0.
     */
    public var tracerefminus: Int by intGreaterThan(0)

    public companion object : SchemeSpec<Error>(::Error)
}

public enum class MeasureMode {
    fraction,
    pixels
}

public enum class ConstrainText {
    inside,
    outside,
    both,
    none
}

public class ColorBar : Scheme() {
    /**
     * Determines whether this color bar's thickness (i.e. the measure
     * in the constant color direction) is set in units of plot "fraction"
     * or in "pixels" (default). Use `thickness` to set the value.
     */
    public var thicknessmode: MeasureMode by enum(MeasureMode.pixels)

    /**
     * Sets the thickness of the color bar.
     * This measure excludes the size of the padding, ticks and labels.
     * Default: 30.
     */
    public var thickness: Number by numberGreaterThan(0)

    /**
     * Determines whether this color bar's length (i.e. the measure
     * in the color variation direction) is set in units of plot
     * "fraction" (default) or in "pixels. Use `len` to set the value.
     */
    public var lenmode: MeasureMode by enum(MeasureMode.fraction)

    /**
     * Sets the length of the color bar This measure excludes
     * the padding of both ends. That is, the color bar length
     * is this length minus the padding on both ends.
     * Default: 1.
     */
    public var len: Number by numberGreaterThan(0)

    /**
     * Sets the x position of the color bar (in plot fraction).
     * Default: 1.02.
     */
    public var x: Number by numberInRange(-2.0..3.0)

    /**
     * Sets this color bar's horizontal position anchor.
     * This anchor binds the `x` position to the "left" (default),
     * "center" or "right" of the color bar.
     */
    public var xanchor: XAnchor by enum(XAnchor.left)

    /**
     * Sets the amount of padding (in px) along the x direction.
     * Default: 10.
     */
    public var xpad: Number by numberGreaterThan(0)

    /**
     * Sets the y position of the color bar (in plot fraction).
     * Default: 0.5.
     */
    public var y: Number by numberInRange(-2.0..3.0)

    /**
     * Sets this color bar's vertical position anchor.
     * This anchor binds the `y` position to the "top",
     * "middle" (default) or "bottom" of the color bar.
     */
    public var yanchor: YAnchor by enum(YAnchor.middle)

    /**
     * Sets the amount of padding (in px) along the y direction.
     * Default: 10.
     */
    public var ypad: Int by intGreaterThan(0)

    /**
     * Sets the axis line color.
     * Default: #444.
     */
    public val bordercolor: Color by color()

    /**
     * Sets the width (in px) or the border enclosing this color bar.
     * Default: 0.
     */
    public var borderwidth: Number by numberGreaterThan(0)

    /**
     * Sets the color of padded area.
     * Default: rgba(0, 0, 0, 0).
     */
    public val bgcolor: Color by color()

    public var title: Title by spec(Title)

    /**
     * Sets the width (in px) of the axis line.
     * Default: 1.
     */
    public var outlinewidth: Number by numberGreaterThan(0)

    /**
     * Sets the axis line color.
     * Default: #444.
     */
    public val outlinecolor: Color by color()

    /**
     * Constrain the size of text inside or outside a bar to be no larger than the bar itself.
     */
    public var constraintext: ConstrainText by enum(ConstrainText.both)

    /**
     * Sets the color bar's tick label font
     */
    public var tickfont: Font by spec(Font)

    public fun title(block: Title.() -> Unit) {
        title = Title(block)
    }

    public fun tickfont(block: Font.() -> Unit) {
        tickfont = Font(block)
    }

    public companion object : SchemeSpec<ColorBar>(::ColorBar)
}

public enum class Orientation {
    v,
    h
}

public enum class ContoursType {
    levels,
    constraint
}

public enum class ContoursColoring {
    fill,
    heatmap,
    lines,
    none
}

public enum class Calendar {
    gregorian,
    chinese,
    coptic,
    discworld,
    ethiopian,
    hebrew,
    islamic,
    julian,
    mayan,
    nanakshahi,
    nepali,
    persian,
    jalali,
    taiwan,
    thai,
    ummalqura
}

public class Domain : Scheme() {
    /**
     * Sets the horizontal domain of this pie trace (in plot fraction).
     * Default: [0, 1]
     */
    public var x: List<Number> by numberList()

    /**
     * Sets the vertical domain of this pie trace (in plot fraction).
     * Default: [0, 1]
     */
    public var y: List<Number> by numberList()

    /**
     * If there is a layout grid, use the domain for this row in the grid for this pie trace.
     */
    public var row: Int by intGreaterThan(0)

    /**
     * If there is a layout grid, use the domain for this column in the grid for this pie trace.
     */
    public var column: Int by intGreaterThan(0)

    public companion object : SchemeSpec<Domain>(::Domain)
}

public class Hoverlabel : Scheme() {

    /**
     * Sets the background color of the hover labels for this trace.
     * */
    public var bgcolor: Color = Color(this, "bgcolor".asName())

    /**
     * Sets the border color of the hover labels for this trace.
     * */
    public var bordercolor: Color = Color(this, "bordercolor".asName())

    /**
     * Sets the font used in hover labels.
     * */
    public var font: Font by spec(Font)

    /**
     * Sets the horizontal alignment of the text content within hover label box. Has an effect
     * only if the hover label text spans more two or more lines.
     *
     * Defaults to `'auto'`.
     * */
    public var align: TraceValues = TraceValues(this, "align".asName())

    /**
     * Sets the default length (in number of characters) of the trace name in the hover labels for all traces.
     * -1 shows the whole name regardless of length. 0-3 shows the first 0-3 characters, and an integer >3 will
     * show the whole name if it is less than that many characters, but if it is longer, will truncate to
     * `namelength - 3` characters and add an ellipsis.
     * */
    public var namelength: Number by numberGreaterThan(-1)

    /**
     * Complementary property to [namelength] to allow passing a list of lengths.
     * */
    public var namelengths: List<Number> by numberList(-1, key = "namelength".asName())

    public fun bgcolors(array: Iterable<Any>) {
        bgcolor.value = array.map { Value.of(it) }.asValue()
    }

    public fun bordercolors(array: Iterable<Any>) {
        bordercolor.value = array.map { Value.of(it) }.asValue()
    }

    public fun font(block: Font.() -> Unit) {
        font = Font(block)
    }

    public fun align(align: HorizontalAlign) {
        align(listOf(align))
    }

    public fun align(alignments: List<HorizontalAlign>) {
        this.align.set(alignments)
    }

    public fun align(vararg alignments: HorizontalAlign) {
        this.align.set(alignments.toList())
    }

    public companion object : SchemeSpec<Hoverlabel>(::Hoverlabel)
}

/**
 * A base class for Plotly traces
 */
public open class Trace : Scheme() {
    public fun axis(axisName: String): TraceValues = TraceValues(this, Name.parse(axisName))

    public val axis: ReadOnlyProperty<Scheme, TraceValues> = ReadOnlyProperty { thisRef, property ->
        TraceValues(thisRef, property.name.asName())
    }

    /**
     * Sets the x coordinates.
     */
    public val x: TraceValues = axis(X_AXIS)

    /**
     * Alternate to `x`. Builds a linear space of x coordinates.
     * Use with `dx` where `x0` is the starting coordinate and `dx` the step.
     * Default: 0.
     */
    public var x0: Value? by value()

    /**
     * Sets the x coordinate step. See `x0` for more info.
     * Default: 1.
     */
    public var dx: Number by numberGreaterThan(0)

    /**
     * Sets the y coordinates.
     */
    public val y: TraceValues = axis(Y_AXIS)

    /**
     * Z coordinates
     */
    public val z: TraceValues = axis(Z_AXIS)

    /**
     * Alternate to `y`. Builds a linear space of y coordinates.
     * Use with `dy` where `y0` is the starting coordinate and `dy` the step.
     */
    public var y0: Value? by value()

    /**
     * Sets the y coordinate step. See `y0` for more info.
     * Default: 1.
     */
    public var dy: Number by numberGreaterThan(0)

    /**
     * Determines whether or not markers and text nodes are clipped about the subplot axes.
     * To show markers and text nodes above axis lines and tick labels, make sure
     * to set `xaxis.layer` and `yaxis.layer` to "below traces".
     */
    public var cliponaxis: Boolean? by boolean()

    /**
     * Determines whether or not the color domain is computed with respect to
     * the input data (here in `z`) or the bounds set in `zmin` and `zmax`
     * Defaults to `false` when `zmin` and `zmax` are set by the user.
     *
     */
    public var zauto: Boolean? by boolean()

    /**
     * Sets the lower bound of the color domain. Value should have the same units
     * as in `z` and if set, `zmax` must be set as well.
     */
    public var zmin: Number? by number()

    /**
     * Sets the upper bound of the color domain. Value should have the same units
     * as in `z` and if set, `zmin` must be set as well.
     */
    public var zmax: Number? by number()

    /**
     * Sets the mid-point of the color domain by scaling `zmin` and/or `zmax`
     * to be equidistant to this point. Value should have the same units as in `z`.
     * Has no effect when `zauto` is `false`.
     */
    public var zmid: Number? by number()

    /**
     * Data array. Sets the values of the sectors.
     * If omitted, we count occurrences of each label.
     */
    public var values: List<Value> by listOfValues()

    /**
     * Data array. Sets the sector labels. If `labels` entries
     * are duplicated, we sum associated `values` or simply
     * count occurrences if `values` is not provided.
     * For other array attributes (including color) we use the first
     * non-empty entry among all occurrences of the label.
     */
    public var labels: List<Value> by listOfValues()

    public var line: LayoutLine by spec(LayoutLine)

    /**
     * Sets the colorscale. The colorscale must be an array
     * containing arrays mapping a normalized value to an rgb,
     * rgba, hex, hsl, hsv, or named color string.
     */
    public var colorscale: Value? by value()

    public var colorbar: ColorBar by spec(ColorBar)

    /**
     * Sets the fill color if `contours.type` is "constraint". Defaults to
     * a half-transparent variant of the line color, marker color, or marker line color, whichever is available.
     */
    public val fillcolor: Color by color()

    /**
     * Sets the trace name. The trace name appear as the legend item and on hover.
     */
    public var name: String? by string()

    public var type: TraceType by enum(TraceType.scatter)

    /**
     * Enumerated , one of ( true | false | "legendonly" )
     * Determines whether or not this trace is visible.
     * If "legendonly", the trace is not drawn, but can appear
     * as a legend item (provided that the legend itself is visible).
     * Default: true.
     */
    public var visible: Visible by enum(Visible.`true`)

    /**
     * Determines whether or not an item corresponding to this trace is shown in the legend.
     * Default: true
     */
    public var showlegend: Boolean? by boolean()

    /**
     * Sets the legend group for this trace. Traces part of
     * the same legend group hide/show at the same time when toggling legend items.
     * Default: ""
     */
    public var legendgroup: String? by string()

    /**
     * Sets the opacity of the trace.
     * Default: 1.
     */
    public var opacity: Number by numberInRange(0.0..1.0)

    //var line by spec(Line)

    public var marker: Marker by spec(Marker)

    /**
     * Sets hover text elements associated with each (x,y) pair. If a single string, the same string appears over all
     * the data points. If an array of string, the items are mapped in order to the this trace's (x,y) coordinates.
     * To be seen, trace `hoverinfo` must contain a "text" flag.
     */
    public val hovertext: TraceValues by axis

    /**
     * Sets text elements associated with each (x,y) pair.
     * The same string appears over all the data points.
     * If trace `hoverinfo` contains a "text" flag and "hovertext" is not set,
     * these elements will be seen in the hover labels.
     */
    public val text: TraceValues = axis(TEXT_AXIS)

    /**
     * Sets the position of the `text` elements
     * with respects to the (x,y) coordinates.
     * Default: "middle center".
     */
    public var textposition: TextPosition by enum(TextPosition.`middle center`)

    /**
     * Sets the positions of the `text` elements
     * with respects to the (x,y) coordinates.
     * Default: "middle center".
     */
    @UnstablePlotlyAPI
    public var textpositionsList: List<Value> by listOfValues(key = "textposition".asName())

    /**
     * Sets the text font.
     */
    public var textfont: Font by spec(Font)

    /**
     * Flaglist string. Any combination of "x", "y", "z", "text", "name" joined with a "+" OR "all" or "none" or "skip".
     * Examples: "x", "y", "x+y", "x+y+z", "all", default: "all". Determines which trace information appear on hover.
     * If `none` or `skip` are set, no information is displayed upon hovering. But, if `none` is set,
     * click and hover events are still fired.
     */
    public var hoverinfo: String? by string()

    public var error_x: Error by spec(Error)

    public var error_y: Error by spec(Error)

    /**
     * Sets the orientation of the plot(s).
     * If "vertical" ("horizontal"), the data
     * is visualized along the vertical (horizontal).
     */
    public var orientation: Orientation by enum(Orientation.h)

    /**
     * If there are multiple violins that should be sized according to
     * some metric (see `scalemode`), link them by providing a non-empty
     * group id here shared by every trace in the same group.
     * If a violin's `width` is undefined, `scalegroup` will default
     * to the trace's name. In this case, violins with the same names
     * will be linked together. Default: ""
     */
    public var scalegroup: String? by string()

    /**
     * Determines whether or not a colorbar is displayed for this trace.
     */
    public var showscale: Boolean? by boolean()

    /**
     * Reverses the color mapping if true. If true, `zmin` will
     * correspond to the last color in the array and `zmax` will correspond to the first color.
     */
    public var reversescale: Boolean? by boolean()

    /**
     * Transposes the z data.
     */
    public var transpose: Boolean? by boolean()

    /**
     * Determines whether or not gaps (i.e. {nan} or missing values) in the `z` data are filled in.
     * It is defaulted to true if `z` is a one dimensional array and `zsmooth` is not false;
     * otherwise it is defaulted to false.
     */
    public var connectgaps: Boolean? by boolean()

    /**
     * Sets the calendar system to use with `x` date data.
     */
    public var xcalendar: Calendar by enum(Calendar.gregorian)

    /**
     * Sets the calendar system to use with `y` date data.
     */
    public var ycalendar: Calendar by enum(Calendar.gregorian)

    public var domain: Domain by spec(Domain)

    public var hoverlabel: Hoverlabel by spec(Hoverlabel)

    public fun values(array: Iterable<Any>) {
        values = array.map { Value.of(it) }
    }

    public fun labels(array: Iterable<Any>) {
        labels = array.map { Value.of(it) }
    }

    public fun colorbar(block: ColorBar.() -> Unit) {
        colorbar = ColorBar(block)
    }

    public fun textfont(block: Font.() -> Unit) {
        textfont = Font(block)
    }

    public fun marker(block: Marker.() -> Unit) {
        marker = Marker(block)
    }

    public fun line(block: LayoutLine.() -> Unit) {
        line = LayoutLine(block)
    }

    public fun error_x(block: Error.() -> Unit) {
        error_x = Error(block)
    }

    public fun error_y(block: Error.() -> Unit) {
        error_y = Error(block)
    }

    public fun domain(block: Domain.() -> Unit) {
        domain = Domain(block)
    }

    public fun hoverlabel(block: Hoverlabel.() -> Unit) {
        hoverlabel = Hoverlabel(block)
    }

    public companion object : SchemeSpec<Trace>(::Trace) {
        public const val X_AXIS: String = "x"
        public const val Y_AXIS: String = "y"
        public const val Z_AXIS: String = "z"
        public const val TEXT_AXIS: String = "text"
    }
}

public operator fun <T : Trace> SchemeSpec<T>.invoke(
    xs: Any,
    ys: Any? = null,
    zs: Any? = null,
    block: Trace.() -> Unit,
): T = invoke {
    x.set(xs)
    if (ys != null) y.set(ys)
    if (zs != null) z.set(zs)
    block()
}
