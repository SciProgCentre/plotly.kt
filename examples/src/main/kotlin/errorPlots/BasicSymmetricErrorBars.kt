package errorPlots

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ErrorType
import space.kscience.plotly.trace

/**
 * - basic symmetric error bars
 * - use numeric array as error length
 * - change color of error bars
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