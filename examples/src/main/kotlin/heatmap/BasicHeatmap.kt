package heatmap

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*
import scientifik.plotly.trace


/**
 * - basic heatmap from 1 to 25
 * - change heatmap colorscale
 * - use 2D array as z
 */
fun main() {
    val x = listOf(1, 2, 3, 4, 5)
    val y = listOf(6, 7, 8, 9, 10)
    val z1 = (1..25).chunked(5)

    val plot = Plotly.plot2D {
        trace(x, y) {
            type = TraceType.heatmap
            z(z1)
            colorscale = Value.of("Reds")
        }

        layout {
            title {
                text = "Red Heatmap"
            }
        }
    }

    plot.makeFile()
}