package scatter

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Scatter
import kscience.plotly.models.ScatterMode

fun main() {
    val scatter = Scatter {
        y.set(List(40) { 5.0 })
        mode = ScatterMode.markers
        marker {
            size = 40
            colors(List(40) { Value.of(it) })
        }
    }

    val plot = Plotly.plot {
        traces(scatter)

        layout {
            title = "Scatter plot with color dimension"
        }
    }

    plot.makeFile()
}
