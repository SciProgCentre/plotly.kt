package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.histogram
import scientifik.plotly.layout
import scientifik.plotly.makeFile
import scientifik.plotly.palettes.T10
import java.util.*


/**
 * - cumulative histogram
 * - use Tableau10 as color palette
 */
fun main() {
    val rnd = Random()
    val values = List(500) { rnd.nextDouble() }

    val plot = Plotly.plot {
        histogram {
            x.numbers = values
            name = "Random data"

            cumulative {
                enabled = true
            }

            marker {
                color(T10.CYAN)
            }
        }

        layout {
            title = "Cumulative Histogram"
            xaxis {
                title = "Value"
            }
            yaxis {
                title = "Sum of probabilities"
            }
        }
    }

    plot.makeFile()
}