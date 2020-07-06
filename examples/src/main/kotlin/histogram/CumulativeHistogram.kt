package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace
import scientifik.plotly.palettes.T10
import java.util.*

/**
 * - cumulative histogram
 * - use Tableau10 as color palette
 */
fun main() {
    val rnd = Random()
    val values = List(500){rnd.nextDouble()}

    val plot = Plotly.plot2D{
        trace(values){
            name = "Random data"
            type = TraceType.histogram

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