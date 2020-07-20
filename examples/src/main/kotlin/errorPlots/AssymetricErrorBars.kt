package errorPlots

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ErrorType
import scientifik.plotly.models.TraceOrder
import scientifik.plotly.models.XAnchor
import scientifik.plotly.models.YAnchor
import scientifik.plotly.scatter

/**
 * - asymmetric error bars
 * - use numeric array as error length
 * - use only negative or positive errors
 * - change legend position
 * - change width of the legend border
 * - change order of legend items
 */
fun main() {
    val x1 = listOf(1, 2, 3, 4)
    val y1 = listOf(2, 3, 5, 8)
    val err = listOf(0.5, 0.75, 1.0, 1.25)

    val plot = Plotly.plot {
        scatter {
            x.set(x1)
            y.set(y1)
            name = "both errors"
            error_y {
                type = ErrorType.data
                array = err
                visible = true
            }
        }

        scatter {
            x.set(x1)
            y.set(y1.map{it + 4}.toList())
            name = "positive err"
            error_y {
                type = ErrorType.data
                array = err
                symmetric = false
                visible = true
            }
        }

        scatter {
            x.set(x1)
            y.set(y1.map{it + 8}.toList())
            name = "negative err"
            error_y {
                type = ErrorType.data
                arrayminus = err
                symmetric = false
                visible = true
            }
        }

        layout{
            title  = "Asymmetric Error Bars"
            legend {
                x = 0.05
                borderwidth = 1
                xanchor = XAnchor.left
                yanchor = YAnchor.top
                traceorder = TraceOrder.reversed
            }
        }
    }

    plot.makeFile()
}