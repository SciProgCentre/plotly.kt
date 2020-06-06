package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace
import java.util.*


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
                title = "Bins"
            }
            yaxis {
                title = "Height"
            }
        }
    }

    plot.makeFile()
}