package kscience.plotly.models

import kscience.plotly.UnstablePlotlyAPI

// Utilities to fill traces with values

/**
 * Append x and y axis values to a trace with optional x error [xErr] and y error [yErr]
 */
@UnstablePlotlyAPI
public fun Trace.appendXY(x: Number, y: Number, xErr: Number? = null, yErr: Number? = null) {
    this.x.numbers += x
    this.y.numbers += y
    xErr?.let { error_x.array += xErr }
    yErr?.let { error_y.array += yErr }
}

/**
 * Append trace values using given [pairs] of x-y number coordinates
 */
@UnstablePlotlyAPI
public fun Trace.appendXY(vararg pairs: Pair<Number, Number>) {
    this.x.numbers += pairs.map { it.first }
    this.y.numbers += pairs.map { it.second }
}

/**
 * Fill trace [x] and [y] with values based on a given integer [xRange] and a numeric [function]. Old values are erased.
 */
@UnstablePlotlyAPI
public fun Trace.functionXY(xRange: IntRange, function: (Int) -> Number) {
    x.numbers = xRange
    y.numbers = xRange.map(function)
}

/**
 * Fill trace [x] and [y] with values based on given [xs] and a [function] for y values. Old values are erased.
 */
@UnstablePlotlyAPI
public fun Trace.functionXY(xs: Iterable<Number>, function: (Double) -> Number) {
    x.numbers = xs
    y.numbers = xs.map { function(it.toDouble()) }
}

/**
 * Fill values in [xRange] with given [step] using given [function]. Old values are erased.
 */
@UnstablePlotlyAPI
public fun Trace.functionXY(
    xRange: ClosedFloatingPointRange<Double>,
    step: Double = 1.0,
    function: (Double) -> Number,
) {
    val xs = buildList {
        var value = xRange.start
        while (value <= xRange.endInclusive) {
            add(value)
            value += step
        }
    }
    x.numbers = xs
    y.numbers = xs.map { function(it) }
}