package space.kscience.plotly

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.dataforge.names.*
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.asValue
import space.kscience.dataforge.values.long
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

//extensions for DataForge


private fun MutableMeta.getIndexedProviders(name: Name): Map<String?, MutableMeta> {
    val parent = getMeta(name.cutLast()) ?: return emptyMap()
    return parent.items.keys.filter {
        it.body == name.lastOrNull()?.body
    }.map {
        it.index
    }.associate { index ->
        if (index == null) {
            "" to getOrCreate(name)
        } else {
            index to getOrCreate(name.withIndex(index))
        }
    }
}

/**
 * A delegate for list of objects with specification
 * TODO move to DataForge core
 */
internal fun <T : Scheme> Configurable.list(
    spec: Specification<T>,
    key: Name? = null,
): ReadWriteProperty<Any?, List<T>> = object : ReadWriteProperty<Any?, List<T>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<T> {
        val name = key ?: property.name.asName()
        return meta.getIndexedProviders(name).values.map { item ->
            spec.write(item)
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>) {
        val name = key ?: property.name.asName()
        meta.setIndexed(name, value.map { it.meta })
    }
}

/**
 * List of values delegate
 */
internal fun Scheme.listOfValues(
    key: Name? = null,
): ReadWriteProperty<Any?, List<Value>> = object : ReadWriteProperty<Any?, List<Value>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<Value> {
        val name = key ?: property.name.asName()
        return meta[name]?.value?.list ?: emptyList()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<Value>) {
        val name = key ?: property.name.asName()
        meta[name] = value.asValue()
    }
}

/**
 * A safe [Double] range
 */
internal fun Scheme.doubleInRange(
    range: ClosedFloatingPointRange<Double>,
    key: Name? = null,
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return meta[name].double ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value in range) {
            meta[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Double] ray
 */
internal fun Scheme.doubleGreaterThan(
    minValue: Double,
    key: Name? = null,
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return meta[name].double ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            meta[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}


/**
 * A safe [Int] ray
 */
internal fun Scheme.intGreaterThan(
    minValue: Int,
    key: Name? = null,
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return meta[name].int ?: minValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            meta[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Int] range
 */
internal fun Scheme.intInRange(
    range: ClosedRange<Int>,
    key: Name? = null,
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return meta[name].int ?: 0
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value in range) {
            meta[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Number] ray
 */
internal fun Scheme.numberGreaterThan(
    minValue: Number,
    key: Name? = null,
): ReadWriteProperty<Any?, Number> = object : ReadWriteProperty<Any?, Number> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Number {
        val name = key ?: property.name.asName()
        return meta[name].number ?: minValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Number) {
        val name = key ?: property.name.asName()
        if (value.toDouble() >= minValue.toDouble()) {
            meta[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Number] range
 */
internal fun Scheme.numberInRange(
    range: ClosedRange<Double>,
    key: Name? = null,
): ReadWriteProperty<Any?, Number> = object : ReadWriteProperty<Any?, Number> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Number {
        val name = key ?: property.name.asName()
        return meta[name].int ?: 0
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Number) {
        val name = key ?: property.name.asName()
        if (value.toDouble() in range) {
            meta[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

@OptIn(DFExperimental::class)
internal fun Scheme.duration(
    default: Duration? = null,
    key: Name? = null,
): ReadWriteProperty<Any?, Duration?> = object : ReadWriteProperty<Any?, Duration?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Duration? {
        val name = key ?: property.name.asName()
        val value = meta[name]?.value
        val units = meta[name + "unit"]?.enum() ?: DurationUnit.MILLISECONDS
        return value?.long?.toDuration(units) ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Duration?) {
        val name = key ?: property.name.asName()
        if (value == null) {
            meta.remove(name)
        } else {
            meta.value = value.toDouble(DurationUnit.MILLISECONDS).asValue()
            meta[name + "unit"] = DurationUnit.MILLISECONDS.name
        }
    }
}

/**
 * Infer type of value and set
 */
public operator fun MutableMeta.set(name: String, value: Any?) {
    when (value) {
        null -> this[name] = null
        is Meta -> this[name] = value
        is Value -> set(Name.parse(name), value)
        is String -> this[name] = value
        is Number -> this[name] = value
        is Boolean -> this[name] = value
        is Iterable<*> -> {
            if (value.all { it is Meta }) {
                this.setIndexed(Name.parse(name), value.map { it as Meta })
            } else {
                this[name] = value.map { Value.of(it) }.asValue()
            }
        }
    }
}

/**
 * A patch to cover API no longer existing in DataForge
 */
@Deprecated("Use specialized meta methods instead")
public operator fun Scheme.set(name: String, value: Any?) {
    meta[name] = value
}