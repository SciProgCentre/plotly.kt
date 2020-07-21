package box

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Box
import scientifik.plotly.models.BoxMean
import scientifik.plotly.models.BoxMode
import scientifik.plotly.models.Orientation


/**
 * - Horizontal box plot
 * - Grouped boxes
 * - Change box colors
 */
fun main() {
    val y0 = listOf("day 1", "day 1", "day 1", "day 1", "day 1", "day 1",
        "day 2", "day 2", "day 2", "day 2", "day 2", "day 2")

    val trace1 = Box {
        x(0.2, 0.2, 0.6, 1.0, 0.5, 0.4, 0.2, 0.7, 0.9, 0.1, 0.5, 0.3)
        y.set(y0)
        name = "kale"
        marker {
            color("#3D9970")
        }
        boxmean = BoxMean.`false`
        orientation = Orientation.h
    }

    val trace2 = Box {
        x(0.6, 0.7, 0.3, 0.6, 0.0, 0.5, 0.7, 0.9, 0.5, 0.8, 0.7, 0.2)
        y.set(y0)
        name = "radishes"
        marker {
            color("#FF4136")
        }
        boxmean = BoxMean.`false`
        orientation = Orientation.h
    }

    val trace3 = Box {
        x(0.1, 0.3, 0.1, 0.9, 0.6, 0.6, 0.9, 1.0, 0.3, 0.6, 0.8, 0.5)
        y.set(y0)
        name = "carrots"
        marker {
            color("#FF851B")
        }
        boxmean = BoxMean.`false`
        orientation = Orientation.h
    }

    val plot = Plotly.plot {
        traces(trace1, trace2, trace3)

        layout {
            title = "Grouped Horizontal Box Plot"
            xaxis {
                title = "normalized moisture"
                zeroline = false
            }
            boxmode = BoxMode.group
        }
    }
    plot.makeFile()
}