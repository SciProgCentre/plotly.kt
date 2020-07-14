package scatter

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.scatter

fun main() {
    val plot = Plotly.plot2D {
        scatter {
            x(1, 2, 3, 4)
            y(10, 15, 13, 17)
            mode = ScatterMode.markers
        }

        scatter {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = ScatterMode.lines
        }

        scatter {
            x(1, 2, 3, 4)
            y(12, 5, 2, 12)
            mode = ScatterMode.`lines+markers`
        }

        layout {
            title {
                text = "Line and Scatter Plot"
            }
        }
    }

    plot.makeFile()
}
