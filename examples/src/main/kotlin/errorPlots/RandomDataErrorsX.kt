package errorPlots

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.palettes.XKCD
import kscience.plotly.trace
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