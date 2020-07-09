package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.asValue
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*
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
    val x = mutableListOf<Double>()
    val y = mutableListOf<Double>()
    val z = mutableListOf<MutableList<Double>>()

    for(i in 0 until size) {
        val elem = -2 * Math.PI + 4 * Math.PI * i / size
        x.add(elem)
        y.add(elem)
        z.add(MutableList(size){0.0})
    }

    for (i in 0 until size) {
        for (j in 0 until size) {
            val r2 = x[i] * x[i] + y[j] * y[j]
            z[i][j] = sin(x[i]) * cos(y[j]) * sin(r2) / ln(r2+1)
        }
    }

    val trace = Trace(x, y) {
        type = TraceType.contour
        z(z)
        colorscale = "YlGnBu".asValue()
    }

    val plot = Plotly.plot2D {
        traces(trace)
        layout {
            title {
                text = "Simple Contour Plot"
            }
        }
    }

    plot.makeFile()
}