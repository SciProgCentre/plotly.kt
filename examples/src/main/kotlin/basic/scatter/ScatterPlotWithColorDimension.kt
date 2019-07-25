package basic.scatter

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Mode
import scientifik.plotly.models.Type
import scientifik.plotly.trace
import kotlin.math.PI
import kotlin.math.sin

fun main() {
    val plot = Plotly.plot2D {
        trace {
            y = List(40) { 5.0 }
            mode = Mode.markers
            marker {
                size = 40
                color = List(40) { it }
            }
        }

        layout {
            title = "ScatterPlotWithColorDimension"
        }
    }

    plot.makeFile()
}
