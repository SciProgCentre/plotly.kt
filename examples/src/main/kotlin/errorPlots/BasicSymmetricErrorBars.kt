package errorPlots

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ErrorType
import scientifik.plotly.trace

/**
 * - basic symmetric error bars
 * - use numeric array as error length
 * - change color or error bars
 */
fun main() {
    val xValues = listOf(0, 1, 2)
    val yValues = listOf(6, 10, 2)
    val err = listOf(1, 2, 3)

    val plot = Plotly.plot {
        trace {
            x.numbers = xValues
            y.numbers = yValues
            error_y {
                type = ErrorType.data
                array = err
                visible = true
                color("orange")
            }
        }

        layout {
            title  = "Basic Symmetric Error Bars"
        }
    }

    plot.makeFile()
}