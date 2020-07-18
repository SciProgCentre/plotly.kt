package scatter

import hep.dataforge.meta.invoke
import hep.dataforge.values.asValue
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Scatter
import scientifik.plotly.models.ScatterMode

fun main() {
    val scatter = Scatter {
        y.set(List(40) { 5.0 })
        mode = ScatterMode.markers
        marker {
            size = 40
            colors(List(40) { it.asValue() })
        }

    }

    val plot = Plotly.plot {
        traces(scatter)

        layout {
            title = "Scatter plot with color dimension"
            title {
                text = "ScatterPlotWithColorDimension"
            }
        }
    }

    plot.makeFile()
}
