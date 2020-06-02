package annotation

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.AxisType
import scientifik.plotly.models.Mode
import scientifik.plotly.text
import scientifik.plotly.trace

fun main() {
    val plot = Plotly.plot2D {
        trace {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = Mode.lines
            type = AxisType.scatter
        }

        text{
            position(2,10)
            text = "start"
        }

        text{
            position(5,17)
            text = "finish"
        }

        layout {
            title = "Plot with annotation"
        }
    }

    plot.makeFile()
}
