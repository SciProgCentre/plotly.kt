package scientifik.plotly

import hep.dataforge.meta.*
import hep.dataforge.names.Name
import hep.dataforge.names.asName
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

/**
 * A delegate for list of objects with specification
 * TODO move to DataForge core
 */
fun <T : Configurable> Configurable.list(
    spec: Specification<T>, key: Name? = null
): ReadWriteProperty<Any?, List<T>> = object : ReadWriteProperty<Any?, List<T>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<T> {
        val name = key ?: property.name.asName()
        return config.getIndexed(name).values.mapNotNull { item -> item.node?.let { spec.wrap(it) } }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>) {
        val name = key ?: property.name.asName()
        config.setIndexed(name, value.map { it.config })
    }
}

/**
 * List of values delegate
 */
fun Configurable.list(
    key: Name? = null
): ReadWriteProperty<Any?, List<Value>> = object : ReadWriteProperty<Any?, List<Value>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<Value> {
        val name = key ?: property.name.asName()
        return config[name].value?.list ?: emptyList()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<Value>) {
        val name = key ?: property.name.asName()
        config[name] = value.asValue()
    }
}

/**
 * A variation of [spec] extension with lazy initialization of empty specified nod in case it is missing
 */
fun <T : Configurable> Configurable.lazySpec(
    spec: Specification<T>, key: Name? = null
): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = key ?: property.name.asName()
        return config[name].node?.let { spec.wrap(it) }
            ?: spec.empty().also { config[name] = it.config }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val name = key ?: property.name.asName()
        config[name] = value.config
    }
}

/**
 * A safe [Double] range
 */
fun Configurable.doubleInRange(
    range: ClosedFloatingPointRange<Double>,
    default: Double? = null,
    key: Name? = null
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return config[name].double ?: default ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value in range) {
            config[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Double] ray
 */
fun Configurable.doubleGreaterThan(
        minValue: Double,
        default: Double? = null,
        key: Name? = null
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return config[name].double ?: default ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            config[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}


/**
 * A safe [Int] ray
 */
fun Configurable.intGreaterThan(
        minValue: Int,
        default: Int? = null,
        key: Name? = null
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return config[name].int ?: default ?: minValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            config[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Int] range
 */
fun Configurable.intInRange(
        range: ClosedRange<Int>,
        default: Int? = null,
        key: Name? = null
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return config[name].int ?: default ?: 0
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


fun Configurable.duration(
    default: Duration? = null,
    key: Name? = null
): ReadWriteProperty<Any?, Duration?> = object : ReadWriteProperty<Any?, Duration?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Duration? {
        val name = key ?: property.name.asName()
        val item = config[name]
        return when (item) {
            null -> null
            is MetaItem.ValueItem -> item.value.long.milliseconds
            is MetaItem.NodeItem<*> -> {
                val value = item.node["value"].long ?: error("Duration value is not defined")
                val unit = item.node["unit"].enum<DurationUnit>() ?: DurationUnit.MILLISECONDS
                value.toDuration(unit)
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Duration?) {
        TODO("Not yet implemented")
    }
}