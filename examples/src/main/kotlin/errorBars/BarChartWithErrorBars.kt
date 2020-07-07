package errorBars

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*


/**
 * - bar chart with symmetrical error bars
 * - use numeric array as error bars length
 * - change color and width of legend border
 */
fun main() {
    val x1 = listOf("Trial 1", "Trial 2", "Trial 3")
    val y1 = listOf(3, 6, 4)
    val y2 = listOf(4, 7, 3)


    val trace1 = Trace(x1, y1) {
        name = "Control"
        error_y {
            type = ErrorType.data
            array = listOf(1, 0.5, 1.5)
            visible = true
        }
        type = TraceType.bar
    }
    val trace2 = Trace(x1, y2) {
        name = "Experimental"
        error_y {
            type = ErrorType.data
            array = listOf(0.5, 1, 2)
            visible = true
        }
        type = TraceType.bar
    }
    val plot = Plotly.plot2D {
        addTrace(trace1, trace2)
        layout {
            barmode = BarMode.group
            title {
                text = "Bar Chart with Error Bars"
            }
            legend {
                x = 1.0
                y = 1.0
                bordercolor("black")
                borderwidth = 1
                xanchor = XAnchor.auto
            }
        }
    }

    plot.makeFile()
}