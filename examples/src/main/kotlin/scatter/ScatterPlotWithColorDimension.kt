package scatter

import hep.dataforge.meta.invoke
import hep.dataforge.values.asValue
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.models.Scatter

fun main() {
    val scatter = Scatter {
        y.set(List(40) { 5.0 })
        mode = ScatterMode.markers
        marker {
            size = 40
            colors(List(40) { it.asValue() })
        }

    }

    val plot = Plotly.plot2D {
        traces(scatter)

        layout {
            title {
                text = "ScatterPlotWithColorDimension"
            }
        }
    }

    plot.makeFile()
}
