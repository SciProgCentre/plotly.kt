package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Contour
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.cos

/**
 * - simple contour plot without level lines
 * - transpose axis
 * - change size of the plot
 * - change color scale
 */
fun main() {
    val size = 25
    val x1 = (0..size).map { it.toDouble() / 5 }
    val y1 = (0..size).map { it.toDouble() / 5 }
    val z = mutableListOf<MutableList<Double>>()

    for (i in 0..size) {
        z.add(MutableList(size+1){0.0})
    }

    for (i in 0..size) {
        for (j in 0..size) {
            z[i][j] = sin(x1[i]).pow(10) + cos(10 + y1[j] * x1[i]) * cos(x1[i])
        }
    }


    val contour = Contour {
        x.set(x1)
        y.set(y1)
        z(z)

        transpose = true
        colorscale = Value.of("Portland")
        reversescale = true

        contours {
            showlines = false
        }
    }

    val plot = Plotly.plot2D {
        traces(contour)

        layout {
            width = 1000
            height = 500
            title {
                text = "Colored Contour Plot"
            }
        }
    }

    plot.makeFile()
}
