package errorBars

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace
import scientifik.plotly.palettes.XKCD
import java.util.*


/**
 * - simple scatter with random length of horizontal error bars
 * - change error bars color
 * - use XKCD as color palette
 */
fun main() {
    val rnd = Random()
    val x = (0..100 step 4).toList().map{ it / 20.0}
    val xerr = List(26){rnd.nextDouble() / 2}

    val plot = Plotly.plot2D {
        trace(x, x) {
            type = TraceType.scatter
            marker {
                color(XKCD.PURPLE)
            }
            error_x {
                array = xerr
                color(XKCD.PALE_PURPLE)
            }
        }
        layout {
            title {
                text = "Random Data Error"
            }
        }
    }

    plot.makeFile()
}