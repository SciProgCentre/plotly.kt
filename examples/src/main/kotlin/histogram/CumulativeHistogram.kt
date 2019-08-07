package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Type
import scientifik.plotly.trace
import java.util.*


fun main() {
    val rnd = Random()
    val x = List(500){rnd.nextDouble()}

    val plot = Plotly.plot2D{
        trace{
            name = "Random data"
            type = Type.histogram

            this.x = x
            cumulative {
                enabled = true
            }
        }
        layout {
            title = "Cumulative Histogram"
            xaxis {
                title = "Bins"
            }
            yaxis {
                title = "Sum of height"
            }
        }
    }

    plot.makeFile()
}