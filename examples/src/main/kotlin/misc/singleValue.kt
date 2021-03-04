package misc

import space.kscience.plotly.Plotly
import space.kscience.plotly.bar
import space.kscience.plotly.makeFile

fun main() {
    val plot = Plotly.plot {
        bar {
            x("giraffes")
            y(20)
        }
    }
    plot.makeFile()
}