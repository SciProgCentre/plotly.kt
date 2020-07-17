package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.histogram
import scientifik.plotly.palettes.T10


/**
 * - horizontal histogram: count entries of each value
 * ([1, 2, 2, 3, 2, 1, 4, 4] -> [1: 2, 2: 3, 3: 1, 4: 2])
 * - use T10 as color palette (default color circle)
 * - use color array
 * - white ticks as space between axis and labels
 * - change ticklen, tickcolor parameters
 */
fun main() {
    val values = listOf(1, 2, 2, 3, 2, 1, 4, 4)
    val colors = listOf(T10.RED, T10.GREEN, T10.ORANGE, T10.BLUE)

    val plot = Plotly.plot2D{
        histogram {
            name = "Random data"
            y.set(values)
            marker {
                colors(colors)
            }
        }

        layout {
            title= "Horizontal Histogram"
            bargap = 0.1
            xaxis {
                title = "Count"
            }
            yaxis {
                title = "Value"
                ticklen = 3
                tickcolor("#FFF")
            }
        }
    }

    plot.makeFile()
}