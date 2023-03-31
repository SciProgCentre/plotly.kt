package plots3d

import space.kscience.dataforge.meta.asValue
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.TraceType
import space.kscience.plotly.trace

private fun l(vararg numbers: Number) = numbers.map { it.asValue() }.asValue()

fun main() {
    val plot = Plotly.plot {
        trace {
            z.value = listOf(
                l(8.83, 8.89, 8.81, 8.87, 8.9, 8.87),
                l(8.89, 8.94, 8.85, 8.94, 8.96, 8.92),
                l(8.84, 8.9, 8.82, 8.92, 8.93, 8.91),
                l(8.79, 8.85, 8.79, 8.9, 8.94, 8.92),
                l(8.79, 8.88, 8.81, 8.9, 8.95, 8.92),
                l(8.8, 8.82, 8.78, 8.91, 8.94, 8.92),
                l(8.75, 8.78, 8.77, 8.91, 8.95, 8.92),
                l(8.8, 8.8, 8.77, 8.91, 8.95, 8.94),
                l(8.74, 8.81, 8.76, 8.93, 8.98, 8.99),
                l(8.89, 8.99, 8.92, 9.1, 9.13, 9.11),
                l(8.97, 8.97, 8.91, 9.09, 9.11, 9.11),
                l(9.04, 9.08, 9.05, 9.25, 9.28, 9.27),
                l(9, 9.01, 9, 9.2, 9.23, 9.2),
                l(8.99, 8.99, 8.98, 9.18, 9.2, 9.19),
                l(8.93, 8.97, 8.97, 9.18, 9.2, 9.18)
            ).asValue()
            type = TraceType.surface
        }
    }
    plot.makeFile()
}