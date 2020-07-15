package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.asValue
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import scientifik.plotly.contour


/**
 * - simple contour plot
 * - change colorscale
 * - set x and y coordinates of the plot
 */
fun main() {
    val size = 100
    val x1 = mutableListOf<Double>()
    val y1 = mutableListOf<Double>()
    val z = mutableListOf<MutableList<Double>>()

    for(i in 0 until size) {
        val elem = -2 * Math.PI + 4 * Math.PI * i / size
        x1.add(elem)
        y1.add(elem)
        z.add(MutableList(size){0.0})
    }

    for (i in 0 until size) {
        for (j in 0 until size) {
            val r2 = x1[i] * x1[i] + y1[j] * y1[j]
            z[i][j] = sin(x1[i]) * cos(y1[j]) * sin(r2) / ln(r2+1)
        }
    }

    val plot = Plotly.plot2D {
        contour {
            x.set(x1)
            y.set(x1)
            z(z)
            colorscale = "YlGnBu".asValue()
        }

        layout {
            title {
                text = "Simple Contour Plot"
            }
        }
    }

    plot.makeFile()
}