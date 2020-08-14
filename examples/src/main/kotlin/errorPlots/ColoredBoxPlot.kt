package errorPlots

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.box
import kscience.plotly.makeFile
import kscience.plotly.palettes.T10
import java.util.*


/**
 * - basic box plot
 * - change color of the boxes
 * - use T10 as color palette
 */
fun main() {
    val rnd = Random()
    val y1 = List(50) { rnd.nextDouble() }
    val y2 = List(50) { rnd.nextDouble() + 1 }

    val plot = Plotly.plot {
        box {
            y.set(y1)
            name = "Sample A"
            marker {
                color(T10.PINK)
            }
        }

        box {
            y.set(y2)
            name = "Sample B"
            marker {
                color(T10.OLIVE)
            }
        }

        layout {
            title = "Colored Box Plot"
        }
    }

    plot.makeFile()
}