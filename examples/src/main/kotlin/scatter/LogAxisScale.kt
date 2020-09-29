package scatter

import kscience.plotly.Plotly
import kscience.plotly.layout
import kscience.plotly.makeFile
import kscience.plotly.models.AxisType
import kscience.plotly.models.Scatter


/**
 * - Scatter plot with logarithmic axes
 */
fun main() {
    val trace1 = Scatter {
        x(0, 1, 2, 3, 4, 5, 6, 7, 8)
        y(8, 7, 6, 5, 4, 3, 2, 1, 0)
    }

    val trace2 = Scatter {
        x(0, 1, 2, 3, 4, 5, 6, 7, 8)
        y(0, 1, 2, 3, 4, 5, 6, 7, 8)
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "Log scale axis"
            xaxis {
                type = AxisType.log
                autorange = true
            }
            yaxis {
                type = AxisType.log
                autorange = true
            }
        }
    }
    plot.makeFile()
}