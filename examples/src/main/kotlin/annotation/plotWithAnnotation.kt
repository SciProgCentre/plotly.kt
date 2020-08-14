package annotation

import kscience.plotly.*
import kscience.plotly.models.ScatterMode

fun main() {
    val plot = Plotly.plot {
        scatter {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = ScatterMode.lines
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
