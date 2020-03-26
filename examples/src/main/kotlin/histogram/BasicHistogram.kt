package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Type
import scientifik.plotly.trace
import java.util.*


fun main() {
    val rnd = Random()
    val values = List(500){rnd.nextDouble()}

    val plot = Plotly.plot2D{
        trace{
            name = "Random data"
            type = Type.histogram
            x(values)
        }
        layout {
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