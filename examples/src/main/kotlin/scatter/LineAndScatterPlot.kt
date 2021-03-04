package scatter

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.scatter


/**
 * - Scatter plot with different scatter modes (markers, lines, markers+lines)
 */
fun main() {
    val plot = Plotly.plot {
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
            title = "Line and Scatter Plot"
        }
    }
    plot.makeFile()
}
