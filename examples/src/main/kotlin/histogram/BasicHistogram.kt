package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace
import java.util.*

/**
 * - basic histogram
 * - change size of gap between bins
 * - change font color and size of title
 * - change font size of axis title
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
            bargap = 0.1
            title {
                text = "Basic Histogram"
                font {
                    size = 20
                    color("black")
                }
            }
            xaxis {
                title {
                    text = "Value"
                    font {
                        size = 16
                    }
                }
            }
            yaxis {
                title {
                    text = "Count"
                    font {
                        size = 16
                    }
                }
            }
        }
    }

    plot.makeFile()
}