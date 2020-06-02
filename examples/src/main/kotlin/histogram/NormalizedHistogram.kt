package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.AxisType
import scientifik.plotly.models.HistNorm
import scientifik.plotly.trace
import java.util.*


fun main() {
    val rnd = Random()
    val x = List(500){rnd.nextDouble()}

    val plot = Plotly.plot2D{
        trace(x){
            name = "Random data"
            type = AxisType.histogram
            histnorm = HistNorm.probability
            marker{
                color(255,255,100)
            }
        }
        layout {
            title = "Normalized Histogram"
        }
    }

    plot.makeFile()
}