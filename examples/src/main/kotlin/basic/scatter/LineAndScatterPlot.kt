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
            x = listOf(1, 2, 3, 4)
            y = listOf(10, 15, 13, 17)
            mode = Mode.markers
            type = Type.scatter
        }
        trace {
            x = listOf(2, 3, 4, 5)
            y = listOf(10, 15, 13, 17)
            mode = Mode.lines
            type = Type.scatter
        }
        trace {
            x = listOf(1, 2, 3, 4)
            y = listOf(12, 5, 2, 12)
            mode = Mode.`lines+markers`
            type = Type.scatter
        }
        layout {
            title = "Line and Scatter Plot"
        }
    }

    plot.makeFile()
}
