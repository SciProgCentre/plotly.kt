package annotation

import scientifik.plotly.*
import scientifik.plotly.models.TraceMode
import scientifik.plotly.models.TraceType

fun main() {
    val plot = Plotly.plot2D {
        trace {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = TraceMode.lines
            type = TraceType.scatter
        }

        text {
            position(2, 10)
            text = "start"
        }

        text {
            position(5, 17)
            text = "finish"
        }

        layout {
            title = "Plot with annotation"
        }
    }

    plot.makeFile()
}
