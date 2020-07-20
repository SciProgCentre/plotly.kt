package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.histogram
import scientifik.plotly.makeFile
import scientifik.plotly.models.HistNorm
import scientifik.plotly.palettes.XKCD
import java.util.*

/**
 * - normalized histogram: the counts normalized to form a probability density,
 * i.e., the area (or integral) under the histogram will sum to 1.
 * - change size of gap between bins
 * - change font size of tick labels
 */
fun main() {
    val rnd = Random()
    val x1 = List(500){rnd.nextDouble()}

    val plot = Plotly.plot{
        histogram {
            x.set(x1)
            name = "Random data"
            histnorm = HistNorm.probability
            marker {
                color(XKCD.BLUE_GREEN)
            }
        }

        layout {
            title= "Normalized Histogram"
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