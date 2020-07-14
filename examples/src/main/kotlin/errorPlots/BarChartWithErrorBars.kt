package errorPlots

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*
import scientifik.plotly.bar


/**
 * - bar chart with symmetrical error bars
 * - use numeric array as error bars length
 * - change color and width of legend border
 */
fun main() {
    val x1 = listOf("Trial 1", "Trial 2", "Trial 3")
    val y1 = listOf(3, 6, 4)
    val y2 = listOf(4, 7, 3)


    val plot = Plotly.plot2D {
        bar {
            x.set(x1)
            y.set(y1)
            name = "Experimental"
            error_y {
                type = ErrorType.data
                array = listOf(1, 0.5, 1.5)
                visible = true
            }
        }

        bar {
            x.set(x1)
            y.set(y2)
            name = "Control"
            error_y {
                type = ErrorType.data
                array = listOf(1, 0.5, 1.5)
                visible = true
            }
        }

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