package contour

import space.kscience.dataforge.meta.asValue
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.contour
import space.kscience.plotly.makeFile
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin


/**
 * - simple contour plot
 * - change colorscale
 * - set x and y coordinates of the plot
 */
fun main() {
    val size = 100
    val x1 = mutableListOf<Double>()
    val y1 = mutableListOf<Double>()
    val z1 = mutableListOf<MutableList<Double>>()

    for (i in 0 until size) {
        val elem = -2 * Math.PI + 4 * Math.PI * i / size
        x1.add(elem)
        y1.add(elem)
        z1.add(MutableList(size) { 0.0 })
    }

    for (i in x1.indices) {
        for (j in y1.indices) {
            val r2 = x1[i] * x1[i] + y1[j] * y1[j]
            z1[i][j] = sin(x1[i]) * cos(y1[j]) * sin(r2) / ln(r2 + 1)
        }
    }

    val plot = Plotly.plot {
        contour {
            x.set(x1)
            y.set(x1)
            z.set(z1)
            colorscale = "YlGnBu".asValue()
        }

        layout {
            title = "Simple Contour Plot"
        }
    }

    plot.makeFile()
}