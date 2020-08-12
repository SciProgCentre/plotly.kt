package errorPlots

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.palettes.XKCD
import scientifik.plotly.trace
import java.util.*


/**
 * - simple scatter with random length of horizontal error bars
 * - change error bars color
 * - use XKCD as color palette
 */
fun main() {
    val rnd = Random()
    val xValues = (0..100 step 4).toList().map { it / 20.0 }
    val err = List(26) { rnd.nextDouble() / 2 }

    val plot = Plotly.plot {
        trace {
            x.numbers = xValues
            marker {
                color(XKCD.PURPLE)
            }
            error_x {
                array = err
                color(XKCD.PALE_PURPLE)
            }
        }
        layout {
            title = "Random Data Error"
        }
    }

    plot.makeFile()
}