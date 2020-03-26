package scientifik.plotly

import hep.dataforge.meta.*
import hep.dataforge.meta.scheme.Configurable
import hep.dataforge.meta.scheme.Specification
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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
 * A variation of [spec] extension which adds
 */
fun <T : Configurable> Configurable.scheme(
    spec: Specification<T>, key: Name? = null
): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = key ?: property.name.asName()
        return config[name].node?.let { spec.wrap(it) } ?: spec.empty().also { config[name] = it.config }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val name = key ?: property.name.asName()
        config[name] = value.config
    }
}