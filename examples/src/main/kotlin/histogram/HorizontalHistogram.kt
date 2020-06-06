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
        trace{
            name = "Random data"
            type = TraceType.histogram
            y.set(values)
            marker {
                color("pink")
            }
        }
        layout {
            title = "Horizontal Histogram"
            xaxis {
                title = "Height"
            }
            yaxis {
                title = "Bins"
            }
        }
    }


    plot.makeFile()
}