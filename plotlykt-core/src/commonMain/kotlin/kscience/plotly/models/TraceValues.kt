package kscience.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.value
import hep.dataforge.names.Name
import hep.dataforge.values.DoubleArrayValue
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import hep.dataforge.values.doubleArray

/**
 * Type-safe accessor class for values in the trace
 */
public class TraceValues internal constructor(public val owner: Scheme, name: Name) {
    public var value: Value? by owner.value(key = name)

    public var doubles: DoubleArray
        get() = value?.doubleArray ?: doubleArrayOf()
        set(value) {
            this.value = DoubleArrayValue(value)
        }

    public var numbers: Iterable<Number>
        get() = value?.list?.map { it.number } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    public var strings: Iterable<String>
        get() = value?.list?.map { it.string } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    /**
     * Smart fill for trace values. The following types are accepted: [DoubleArray], [IntArray], [Array] of primitive or string,
     * [Iterable] of primitive or string.
     */
    public fun set(values: Any?) {
        value = when (values) {
            null -> null
            is DoubleArray -> values.asValue()
            is IntArray -> values.map { it.asValue() }.asValue()
            is Array<*> -> values.map { Value.of(it) }.asValue()
            is Iterable<*> -> values.map { Value.of(it) }.asValue()
            else -> error("Unrecognized values type ${values::class}")
        }
    }

    public operator fun invoke(vararg numbers: Number) {
        this.numbers = numbers.asList()
    }

    public operator fun invoke(vararg strings: String) {
        this.strings = strings.asList()
    }

    public operator fun invoke(lists: List<List<Number>>) {
        this.value = lists.map { row -> row.map { it.asValue() }.asValue() }.asValue()
    }

}