package errorPlots

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*
import scientifik.plotly.trace
import scientifik.plotly.palettes.T10
import java.util.*


/**
 * - basic box plot
 * - change color of the boxes
 * - use T10 as color palette
 */
fun main() {
    val rnd = Random()
    val y1 = List(50){ rnd.nextDouble() }
    val y2 = List(50){ rnd.nextDouble() + 1}

    val plot = Plotly.plot2D {
        trace {
            y.set(y1)
            type = TraceType.box
            name = "Sample A"
            marker {
                color(T10.PINK)
            }
        }

        trace {
            y.set(y2)
            type = TraceType.box
            name = "Sample B"
            marker {
                color(T10.OLIVE)
            }
        }
        layout {
            title {
                text = "Colored Box Plot"
            }
        }
    }

    plot.makeFile()
}