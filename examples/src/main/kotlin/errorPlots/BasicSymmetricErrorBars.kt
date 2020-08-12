package errorPlots

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.ErrorType
import kscience.plotly.trace

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
            title = "Basic Symmetric Error Bars"
        }
    }

    plot.makeFile()
}