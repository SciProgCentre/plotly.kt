package errorPlots

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.palettes.Xkcd
import space.kscience.plotly.trace
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
                color(Xkcd.PURPLE)
            }
            error_x {
                array = err
                color(Xkcd.PALE_PURPLE)
            }
        }
        layout {
            title = "Random Data Error"
        }
    }

    plot.makeFile()
}