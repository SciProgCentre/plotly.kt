package misc

import kscience.plotly.Plotly
import kscience.plotly.bar
import kscience.plotly.makeFile

fun main() {
    val plot = Plotly.plot {
        bar {
            x("giraffes")
            y(20)
        }
    }
    plot.makeFile()
}