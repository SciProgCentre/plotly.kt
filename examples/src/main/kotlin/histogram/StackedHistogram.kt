package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.BarMode
import scientifik.plotly.models.Trace
import scientifik.plotly.models.Type
import java.util.*


fun main() {
    val rnd = Random()
    val k = List(500) { rnd.nextDouble() }
    val x1 = k.map { it }.toList()
    val x2 = k.map { it/2 }.toList()

    val trace1 = Trace(x1) {
        type = Type.histogram
        opacity = 0.5
        marker {
            color("green")
        }
    }
    val trace2 = Trace(x2) {
        type = Type.histogram
        opacity = 0.6
        marker {
            color("red")
        }
    }
    val plot = Plotly.plot2D {
        addTrace(trace1, trace2)
        layout {
            title = "Stacked Histogram"
            barmode = BarMode.stack
        }
    }


    plot.makeFile()
}