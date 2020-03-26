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
            y(values)
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