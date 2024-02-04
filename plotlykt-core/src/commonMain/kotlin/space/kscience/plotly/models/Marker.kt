package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.asName
import space.kscience.plotly.intGreaterThan
import space.kscience.plotly.listOfValues
import space.kscience.plotly.numberGreaterThan
import space.kscience.plotly.numberInRange

public enum class GradientType {
    radial,
    horizontal,
    vertical,
    none
}

public class Gradient : Scheme() {
    /**
     * Sets the final color of the gradient fill: the center color for radial,
     * the right for horizontal, or the bottom for vertical.
     */
    public val color: Color by color()

    /**
     * Sets the type of gradient used to fill the markers
     */
    public var type: GradientType by enum(GradientType.none)

    /**
     * Sets the type of gradient used to fill the markers
     */
    public var typesList: List<Value> by listOfValues()

    public fun colors(colors: Iterable<Any>) {
        color.value = colors.map { Value.of(it) }.asValue()
    }

    public fun typesList(array: Iterable<Any>) {
        typesList = array.map { Value.of(it) }
    }

    public companion object : SchemeSpec<Gradient>(::Gradient)
}

public class Marker : Scheme() {
    /**
     * Sets the marker symbol type.
     * Default: circle.
     */
    public var symbol: Symbol by enum(Symbol.circle)

    /**
     * Array of enumerateds. Sets the marker symbol type.
     */
    public var symbolsList: List<Value> by listOfValues(key = "symbol".asName())

    /**
     * Sets the marker size (in px).
     * Default: 6.
     */
    public var size: Number by numberGreaterThan(0)

    /**
     * Array of numbers greater than or equal to 0.
     * Sets the markers size. Default: 6.
     */
    public var sizesList: List<Number> by numberList(key = "size".asName())

    /**
     * Sets the marker opacity.
     */
    public var opacity: Number by numberInRange(0.0..1.0)

    /**
     * Sets the markers opacity.
     */
    public var opacitiesList: List<Number> by numberList(key = "opacity".asName())

    /**
     * Sets a maximum number of points to be drawn on the graph.
     * "0" corresponds to no limit.
     * Default: 0.
     */
    public var maxdisplayed: Int by intGreaterThan(0)

    /**
     * Has an effect only if `size` is set to a numerical array.
     * Sets the scale factor used to determine the rendered size
     * of marker points. Use with `sizemin` and `sizemode`.
     * Default: 1.
     */
    public var sizeref: Number? by number()

    /**
     * Has an effect only if `marker.size` is set to a numerical array.
     * Sets the minimum size (in px) of the rendered marker points.
     * Default: 0.
     */
    public var sizemin: Number by numberGreaterThan(0)

    /**
     * Enumerated , one of ( "diameter" | "area" )
     * Has an effect only if `marker.size` is set to a numerical array.
     * Sets the rule for which the data in `size` is converted to pixels.
     * Default: "diameter".
     */
    public var sizemode: SizeMode by enum(SizeMode.diameter)

    public var line: MarkerLine by scheme(MarkerLine)

    /**
     * Sets the marker color. It accepts either a specific color or an array of numbers that are mapped to the colorscale
     * relative to the max and min values of the array or relative to `marker.cmin` and `marker.cmax` if set.
     */
    public val color: Color by color()

    /**
     * Sets the color of each sector. If not specified, the default trace color set is used to pick the sector colors.
     */
    public var pieColors: List<Value> by listOfValues(key = "colors".asName())

    public var colorbar: ColorBar by scheme(ColorBar)

    public var gradient: Gradient by scheme(Gradient)

    /**
     * Sets the color of the outlier sample points. Default: rgba(0, 0, 0, 0).
     */
    public val outliercolor: Color by color()

    public fun colors(colors: Iterable<Any>) {
        color.value = colors.map { Value.of(it) }.asValue()
    }

    public fun line(block: MarkerLine.() -> Unit) {
        line = MarkerLine(block)
    }

    public fun colorbar(block: ColorBar.() -> Unit) {
        colorbar = ColorBar(block)
    }

    public fun gradient(block: Gradient.() -> Unit) {
        gradient = Gradient(block)
    }

    public companion object : SchemeSpec<Marker>(::Marker)
}

