package kscience.plotly.models

import hep.dataforge.meta.value
import hep.dataforge.names.asName
import hep.dataforge.values.DoubleArrayValue
import hep.dataforge.values.Value
import hep.dataforge.values.asValue
import hep.dataforge.values.doubleArray

/**
 * Type-safe accessor class for values in the trace
 */
class TraceValues internal constructor(val trace: Trace, axis: String) {
    var value by trace.value(key = axis.asName())

    var doubles: DoubleArray
        get() = value?.doubleArray ?: doubleArrayOf()
        set(value) {
            this.value = DoubleArrayValue(value)
        }

    var numbers: Iterable<Number>
        get() = value?.list?.map { it.number } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    var strings: Iterable<String>
        get() = value?.list?.map { it.string } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    /**
     * Smart fill for trace values. The following types are accepted: [DoubleArray], [IntArray], [Array] of primitive or string,
     * [Iterable] of primitive or string.
     */
    fun set(values: Any?) {
        value = when (values) {
            null -> null
            is DoubleArray -> values.asValue()
            is IntArray -> values.map { it.asValue() }.asValue()
            is Array<*> -> values.map { Value.of(it) }.asValue()
            is Iterable<*> -> values.map { Value.of(it) }.asValue()
            else -> error("Unrecognized values type ${values::class}")
        }
    }

    operator fun invoke(vararg numbers: Number) {
        this.numbers = numbers.asList()
    }

    operator fun invoke(vararg strings: String) {
        this.strings = strings.asList()
    }

    operator fun invoke(lists: List<List<Number>>) {
        this.value = lists.map { row -> row.map { it.asValue() }.asValue() }.asValue()
    }

}