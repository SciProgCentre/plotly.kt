package space.kscience.plotly.models

import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.value
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.asValue
import space.kscience.dataforge.values.string
import kotlin.properties.ReadOnlyProperty

/**
 * A color value customizer
 * TODO add a hook for descriptor generation
 */
public class Color internal constructor(parent: Scheme, key: Name) {
    public var value: Value? by parent.value(key = key)

    public var string: String?
        get() = value?.string
        set(value) {
            this.value = value?.asValue()
        }

    public operator fun invoke(value: String): Unit {
        this.value = value.asValue()
    }

    public operator fun invoke(value: Number): Unit {
        this.value = value.asValue()
    }

    public operator fun invoke(red: Number, green: Number, blue: Number) {
        invoke("rgb(${red.toFloat()},${green.toFloat()},${blue.toFloat()})")
    }

    public operator fun invoke(red: Number, green: Number, blue: Number, alpha: Number) {
        invoke("rgba(${red.toFloat()},${green.toFloat()},${blue.toFloat()},${alpha.toFloat()})")
    }
}

public fun Scheme.color(key: Name? = null): ReadOnlyProperty<Scheme, Color> = ReadOnlyProperty { _, property ->
    Color(this, key ?: property.name.asName())
}