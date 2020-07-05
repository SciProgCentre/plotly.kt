package scientifik.plotly.models

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue

class Marker : Scheme() {
    var symbol: Symbol by enum(Symbol.circle)
    var size by int(6)
    var opacity by double() // FIXME("number between or equal to 0 and 1")
    var maxdisplayed by int(0)
    var sizeref by int(1)
    var sizemin by int(0) // FIXME("number greater than or equal to 0")
    var sizemode by enum(SizeMode.diameter)
    var line by spec(MarkerLine)

    val color = Color(this, "color".asName())

    fun colors(colors: Iterable<Any>) {
        color.value = colors.map { Value.of(it) }.asValue()
    }

    fun line(block: MarkerLine.() -> Unit) {
        line = MarkerLine(block)
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