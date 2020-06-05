package scatter

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.AxisType
import scientifik.plotly.models.TraceMode
import scientifik.plotly.trace

fun main() {
    val plot = Plotly.plot2D {
        trace {
            x(1, 2, 3, 4)
            y(10, 15, 13, 17)
            mode = TraceMode.markers
            type = AxisType.scatter
        }
        trace {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = TraceMode.lines
            type = AxisType.scatter
        }
        trace {
            x(1, 2, 3, 4)
            y(12, 5, 2, 12)
            mode = TraceMode.`lines+markers`
            type = AxisType.scatter
        }
        layout {
            title = "Line and Scatter Plot"
        }
    }

    plot.makeFile()
}
