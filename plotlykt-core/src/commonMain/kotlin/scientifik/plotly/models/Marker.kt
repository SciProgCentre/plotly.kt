package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import scientifik.plotly.intGreaterThan
import scientifik.plotly.list
import scientifik.plotly.numberGreaterThan
import scientifik.plotly.numberInRange

enum class GradientType {
    radial,
    horizontal,
    vertical,
    none
}

class Gradient : Scheme() {
    /**
     * Sets the final color of the gradient fill: the center color for radial,
     * the right for horizontal, or the bottom for vertical.
     */
    var color = Color(this, "color".asName())

    /**
     * Sets the type of gradient used to fill the markers
     */
    var type by enum(GradientType.none)

    companion object : SchemeSpec<Gradient>(::Gradient)
}

class Marker : Scheme() {
    /**
     * Sets the marker symbol type.
     * Default: circle.
     */
    var symbol: Symbol by enum(Symbol.circle)

    /**
     * Sets the marker size (in px).
     * Default: 6.
     */
    var size by intGreaterThan(0)

    /**
     * Sets the marker opacity.
     */
    var opacity by numberInRange(0.0..1.0)

    /**
     * Sets a maximum number of points to be drawn on the graph.
     * "0" corresponds to no limit.
     * Default: 0.
     */
    var maxdisplayed by intGreaterThan(0)

    /**
     * Has an effect only if `size` is set to a numerical array.
     * Sets the scale factor used to determine the rendered size
     * of marker points. Use with `sizemin` and `sizemode`.
     * Default: 1.
     */
    var sizeref by number()

    /**
     * Has an effect only if `marker.size` is set to a numerical array.
     * Sets the minimum size (in px) of the rendered marker points.
     * Default: 0.
     */
    var sizemin by numberGreaterThan(0)

    /**
     * Enumerated , one of ( "diameter" | "area" )
     * Has an effect only if `marker.size` is set to a numerical array.
     * Sets the rule for which the data in `size` is converted to pixels.
     * Default: "diameter".
     */
    var sizemode by enum(SizeMode.diameter)

    var line by spec(MarkerLine)

    /**
     * Sets themarkercolor. It accepts either a specific color or an array of numbers that are mapped to the colorscale
     * relative to the max and min values of the array or relative to `marker.cmin` and `marker.cmax` if set.
     */
    val color = Color(this, "color".asName())

    /**
     * Sets the color of each sector. If not specified, the default trace color set is used to pick the sector colors.
     */
    var pieColors by list(key="colors".asName())

    var colorbar by spec(ColorBar)

    var gradient by spec(Gradient)

    /**
     * Sets the color of the outlier sample points. Default: rgba(0, 0, 0, 0).
     */
    var outliercolor = Color(this, "outliercolor".asName())

    fun colors(colors: Iterable<Any>) {
        color.value = colors.map { Value.of(it) }.asValue()
    }

    fun line(block: MarkerLine.() -> Unit) {
        line = MarkerLine(block)
    }

    fun colorbar(block: ColorBar.() -> Unit) {
        colorbar = ColorBar(block)
    }

    fun gradient(block: Gradient.() -> Unit) {
        gradient = Gradient(block)
    }

    companion object : SchemeSpec<Marker>(::Marker)
}

/**
 * A color value customizer
 * TODO add a hook for descriptor generation
 */
class Color internal constructor(parent: Scheme, key: Name) {
    var value by parent.value(key = key)

    var string
        get() = value?.string
        set(value) {
            this.value = value?.asValue()
        }

    operator fun invoke(value: String): Unit {
        this.value = value.asValue()
    }

    operator fun invoke(value: Number): Unit {
        this.value = value.asValue()
    }

    operator fun invoke(red: Number, green: Number, blue: Number) {
        invoke("rgb(${red.toFloat()},${green.toFloat()},${blue.toFloat()})")
    }

    operator fun invoke(red: Number, green: Number, blue: Number, alpha: Number) {
        invoke("rgba(${red.toFloat()},${green.toFloat()},${blue.toFloat()},${alpha.toFloat()})")
    }
}