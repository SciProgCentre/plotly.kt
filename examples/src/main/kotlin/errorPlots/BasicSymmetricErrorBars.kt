package errorPlots

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ErrorType
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace

/**
 * - basic symmetric error bars
 * - use numeric array as error length
 * - change color or error bars
 */
fun main() {
    val values_x = listOf(0, 1, 2)
    val values_y = listOf(6, 10, 2)
    val values_err = listOf(1, 2, 3)

    val plot = Plotly.plot2D{
        trace(values_x, values_y){
            type = TraceType.scatter
            error_y {
                type = ErrorType.data
                array = values_err
                visible = true
                color("orange")
            }
        }
        layout{
            title {
                text = "Basic Symmetric Error Bars"
            }
        }
    }

    plot.makeFile()
}