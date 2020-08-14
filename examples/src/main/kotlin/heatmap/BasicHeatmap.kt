package heatmap

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.heatmap
import kscience.plotly.makeFile


/**
 * - basic heatmap from 1 to 25
 * - change heatmap colorscale
 * - use 2D array as z
 */
fun main() {
    val x1 = listOf(1, 2, 3, 4, 5)
    val y1 = listOf(6, 7, 8, 9, 10)
    val z1 = (1..25).chunked(5)

    val plot = Plotly.plot {
        heatmap {
            x.set(x1)
            y.set(y1)
            z.set(z1)
            colorscale = Value.of("Reds")
        }

        layout {
            title = "Red Heatmap"
        }
    }

    plot.makeFile()
}