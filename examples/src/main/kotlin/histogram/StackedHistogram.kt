package histogram

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.BarMode
import space.kscience.plotly.models.Histogram
import java.util.*


fun main() {
    val rnd = Random()
    val k = List(500) { rnd.nextDouble() }
    val x1 = k.map { it }.toList()
    val x2 = k.map { it / 2 }.toList()

    val trace1 = Histogram {
        x.set(x1)
        opacity = 0.5
        marker {
            color("green")
        }
    }

    val trace2 = Histogram {
        x.set(x2)
        opacity = 0.5
        marker {
            color("orange")
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)
        layout {
            title = "Stacked Histogram"
            barmode = BarMode.stack
        }
    }

    plot.makeFile()
}