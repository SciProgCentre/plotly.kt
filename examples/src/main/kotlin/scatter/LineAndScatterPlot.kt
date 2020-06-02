package scatter

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.AxisType
import scientifik.plotly.models.Mode
import scientifik.plotly.trace

fun main() {
    val plot = Plotly.plot2D {
        trace {
            x(1, 2, 3, 4)
            y(10, 15, 13, 17)
            mode = Mode.markers
            type = AxisType.scatter
        }
        trace {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = Mode.lines
            type = AxisType.scatter
        }
        trace {
            x(1, 2, 3, 4)
            y(12, 5, 2, 12)
            mode = Mode.`lines+markers`
            type = AxisType.scatter
        }
        layout {
            title = "Line and Scatter Plot"
        }
    }

    plot.makeFile()
}
