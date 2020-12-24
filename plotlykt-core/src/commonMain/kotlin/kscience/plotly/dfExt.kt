package kscience.plotly

import hep.dataforge.meta.*
import hep.dataforge.names.*
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import hep.dataforge.values.long
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.milliseconds
import kotlin.time.toDuration

//extensions for DataForge


private fun MutableItemProvider.getIndexedProviders(name: Name): Map<String?, MutableItemProvider> {
    val parent = getItem(name.cutLast()).node ?: return emptyMap()
    return parent.items.keys.filter { it.body == name.lastOrNull()?.body }.mapNotNull { it.index }.associate { index ->
        index to getChild(name.withIndex(index))
    }
}


/**
 * A delegate for list of objects with specification
 * TODO move to DataForge core
 */
internal fun <T : Scheme> MutableItemProvider.list(
    spec: Specification<T>,
    key: Name? = null,
): ReadWriteProperty<Any?, List<T>> = object : ReadWriteProperty<Any?, List<T>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<T> {
        val name = key ?: property.name.asName()
        return getIndexedProviders(name).values.map { item ->
            spec.write(item)
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>) {
        val name = key ?: property.name.asName()
        setIndexed(name, value.mapNotNull { it.rootNode })
    }
}

/**
 * List of values delegate
 */
internal fun MutableItemProvider.listOfValues(
    key: Name? = null,
): ReadWriteProperty<Any?, List<Value>> = object : ReadWriteProperty<Any?, List<Value>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<Value> {
        val name = key ?: property.name.asName()
        return getItem(name).value?.list ?: emptyList()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<Value>) {
        val name = key ?: property.name.asName()
        set(name, value.asValue())
    }
}

/**
 * A variation of [spec] extension with lazy initialization of empty specified nod in case it is missing
 */
internal fun <T : Scheme> MutableItemProvider.lazySpec(
    spec: Specification<T>, key: Name? = null,
): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = key ?: property.name.asName()
        return spec.write(getChild(name))
//        return (getItem(name).node as? MutableItemProvider)?.let {
//            spec.write(it)
//        } ?: spec.empty().also { setItem(name, it[Name.EMPTY]) }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val name = key ?: property.name.asName()
        set(name, value.rootNode)
    }
}

/**
 * A safe [Double] range
 */
internal fun MutableItemProvider.doubleInRange(
    range: ClosedFloatingPointRange<Double>,
    key: Name? = null,
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return this@doubleInRange[name].double ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value in range) {
            this@doubleInRange[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Double] ray
 */
internal fun MutableItemProvider.doubleGreaterThan(
    minValue: Double,
    key: Name? = null,
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return this@doubleGreaterThan[name].double ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            this@doubleGreaterThan[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}


/**
 * A safe [Int] ray
 */
internal fun MutableItemProvider.intGreaterThan(
    minValue: Int,
    key: Name? = null,
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return this@intGreaterThan[name].int ?: minValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            this@intGreaterThan[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Int] range
 */
internal fun Configurable.intInRange(
    range: ClosedRange<Int>,
    key: Name? = null,
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return config[name].int ?: 0
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value in range) {
            config[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Number] ray
 */
internal fun MutableItemProvider.numberGreaterThan(
    minValue: Number,
    key: Name? = null,
): ReadWriteProperty<Any?, Number> = object : ReadWriteProperty<Any?, Number> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Number {
        val name = key ?: property.name.asName()
        return this@numberGreaterThan[name].number ?: minValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Number) {
        val name = key ?: property.name.asName()
        if (value.toDouble() >= minValue.toDouble()) {
            this@numberGreaterThan[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Number] range
 */
internal fun MutableItemProvider.numberInRange(
    range: ClosedRange<Double>,
    key: Name? = null,
): ReadWriteProperty<Any?, Number> = object : ReadWriteProperty<Any?, Number> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Number {
        val name = key ?: property.name.asName()
        return this@numberInRange[name].int ?: 0
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Number) {
        val name = key ?: property.name.asName()
        if (value.toDouble() in range) {
            this@numberInRange[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

@OptIn(DFExperimental::class)
internal fun MutableItemProvider.duration(
    default: Duration? = null,
    key: Name? = null,
): ReadWriteProperty<Any?, Duration?> = object : ReadWriteProperty<Any?, Duration?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Duration? {
        val name = key ?: property.name.asName()
        return when (val item = getItem(name)) {
            null -> default
            is ValueItem -> item.value.long.milliseconds
            is NodeItem -> {
                val value = item.node["value"].long ?: error("Duration value is not defined")
                val unit = item.node["unit"].enum<DurationUnit>() ?: DurationUnit.MILLISECONDS
                value.toDuration(unit)
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Duration?) {
        val name = key ?: property.name.asName()
        if (value == null) {
            remove(name)
        } else {
            set(name + "value", value.inMilliseconds)
            set(name + "unit", DurationUnit.MILLISECONDS)
        }
    }
}