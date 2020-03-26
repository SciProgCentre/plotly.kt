package basic.scatter

import hep.dataforge.values.asValue
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Mode
import scientifik.plotly.trace

fun main() {
    val plot = Plotly.plot2D {
        trace {
            y(List(40) { 5.0 })
            mode = Mode.markers
            marker {
                size = 40
                colors(List(40) { it.asValue() })
            }
        }

        layout {
            title = "ScatterPlotWithColorDimension"
        }
    }

    plot.makeFile()
}
