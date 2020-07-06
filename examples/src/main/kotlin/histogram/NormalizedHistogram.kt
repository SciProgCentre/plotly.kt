package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.HistNorm
import scientifik.plotly.models.TraceType
import scientifik.plotly.palettes.XKCD
import scientifik.plotly.trace
import java.util.*

/**
 * - normalized histogram
 * - change gap between bins
 * - change font size of tick labels
 */
fun main() {
    val rnd = Random()
    val x = List(500){rnd.nextDouble()}

    val plot = Plotly.plot2D{
        trace(x){
            name = "Random data"
            type = TraceType.histogram
            histnorm = HistNorm.probability
            marker{
                color(XKCD.BLUE_GREEN)
            }
        }
        layout {
            title = "Normalized Histogram"
            bargap = 0.1
            xaxis {
                tickfont {
                    size = 16
                }
            }
            yaxis {
                tickfont {
                    size = 16
                }
            }
        }
    }

    plot.makeFile()
}