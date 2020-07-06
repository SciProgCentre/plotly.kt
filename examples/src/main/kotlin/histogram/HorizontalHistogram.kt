package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace
import scientifik.plotly.palettes.T10


/**
 * - horizontal histogram
 * - use T10 as color palette (default color circle)
 * - use color array
 * - white ticks as space between axis and labels
 * - change ticklen, tickcolor parameters
 */
fun main() {
    val values = listOf(1, 2, 2, 3, 2, 1, 4, 4)
    val colors = listOf(T10.BLUE, T10.ORANGE, T10.GREEN, T10.RED)

    val plot = Plotly.plot2D{
        trace {
            name = "Random data"
            type = TraceType.histogram
            y.set(values)
            marker {
                colors(colors.reversed())
            }
        }
        layout {
            title = "Horizontal Histogram"
            xaxis {
                title = "Count"
            }
            yaxis {
                title = "Value"
                ticklen = 3
                tickcolor("#FFF")
            }
            bargap = 0.1
        }
    }


    plot.makeFile()
}