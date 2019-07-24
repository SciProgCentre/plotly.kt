import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Type
import scientifik.plotly.trace
import java.util.*


fun main() {

    val rnd = Random()
    val x = List(500){rnd.nextDouble()}


    val plot = Plotly.plot2D {
        trace{
            name = "Random data"
            type = Type.histogram
            this.x = x
        }
        layout {
            title = "Simple histogram plot"
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