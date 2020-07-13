package scatter

import hep.dataforge.meta.invoke
import hep.dataforge.values.asValue
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceMode
import scientifik.plotly.trace

fun main() {
    val plot = Plotly.plot2D {
        trace {
            y.set(List(40) { 5.0 })
            mode = TraceMode.markers
            marker {
                size = 40
                colors(List(40) { it.asValue() })
            }
        }

        layout {
            title = "Scatter plot with color dimension"
        }
    }

    plot.makeFile()
}
