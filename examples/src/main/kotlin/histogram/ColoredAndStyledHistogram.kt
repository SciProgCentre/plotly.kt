package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.BarMode
import scientifik.plotly.models.HisFunc
import scientifik.plotly.models.Trace
import scientifik.plotly.models.Type
import java.util.*


fun main() {
    val rnd = Random()
    val k = List(500) { rnd.nextDouble() }
    val x1 = k.map { it * 5 }.toList()
    val x2 = k.map { it * 10 }.toList()
    val y1 = k.map { it }.toList()
    val y2 = k.map { it * 2 }.toList()

    val trace1 = Trace(x1, y1) {
        name = "control"
        histfunc = HisFunc.count
        marker {
            color(255, 100, 102, 0.7)
        }
        opacity = 0.5
        type = Type.histogram
        xbins {
            end = 2.8
            start = 0.5
            size = 0.06
        }
    }

    val trace2 = Trace(x2, y2) {
        name = "experimental"
        marker {
            color(100, 200, 102, 0.7)
        }
        opacity = 0.75
        type = Type.histogram
        xbins {
            end = 4.0
            start = -3.2
            size = 0.06
        }
    }

    val plot = Plotly.plot2D {
        addTrace(trace1, trace2)
        layout {
            bargap = 0.05
            bargroupgap = 0.2
            barmode = BarMode.overlay
            title = "Sampled Results"
            xaxis {
                title = "Value"
            }
            yaxis {
                title = "Count"
            }
        }
    }

    plot.makeFile()
}