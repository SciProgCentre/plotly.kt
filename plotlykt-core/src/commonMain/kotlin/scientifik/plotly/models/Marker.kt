package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import scientifik.plotly.doubleInRange
import scientifik.plotly.intGreaterThan

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
    var opacity by doubleInRange(0.0..1.0)

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
    var sizeref by int()

    /**
     * Has an effect only if `marker.size` is set to a numerical array.
     * Sets the minimum size (in px) of the rendered marker points.
     * Default: 0.
     */
    var sizemin by intGreaterThan(0)

    /**
     * Enumerated , one of ( "diameter" | "area" )
     * Has an effect only if `marker.size` is set to a numerical array.
     * Sets the rule for which the data in `size` is converted to pixels.
     * Default: "diameter".
     */
    var sizemode by enum(SizeMode.diameter)

    var line by spec(MarkerLine)

    val color = Color(this, "color".asName())

    var colorbar by spec(ColorBar)

    fun colors(colors: Iterable<Any>) {
        color.value = colors.map { Value.of(it) }.asValue()
    }

    fun line(block: MarkerLine.() -> Unit) {
        line = MarkerLine(block)
    }

    fun colorbar(block: ColorBar.() -> Unit) {
        colorbar = ColorBar(block)
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