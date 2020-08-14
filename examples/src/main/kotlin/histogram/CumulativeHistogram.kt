package histogram

import kscience.plotly.Plotly
import kscience.plotly.histogram
import kscience.plotly.layout
import kscience.plotly.makeFile
import kscience.plotly.palettes.T10
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