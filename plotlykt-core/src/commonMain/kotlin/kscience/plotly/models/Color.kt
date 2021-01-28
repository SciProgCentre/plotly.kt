package kscience.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.value
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import hep.dataforge.values.string
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