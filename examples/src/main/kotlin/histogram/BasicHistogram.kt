package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace
import java.util.*

/**
 * - basic histogram
 * - change bargap parameter
 */
fun main() {
    val rnd = Random()
    val values = List(500){rnd.nextDouble()}

    val plot = Plotly.plot2D{
        trace(values){
            name = "Random data"
            type = TraceType.histogram
        }
        layout{
            title = "Basic Histogram"
            xaxis {
                title = "Value"
            }
            yaxis {
                title = "Count"
            }
            bargap = 0.1
        }
    }

    plot.makeFile()
}