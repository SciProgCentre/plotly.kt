package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.contour
import scientifik.plotly.models.ContoursColoring
import kotlin.math.pow
import kotlin.math.exp

/**
 * - colored contour plot lines
 * - labels on contour lines
 * - change size of the plot and contour lines width
 */
fun main() {
    val range = -300..300
    val rangeSize = range.last - range.first + 1
    val x1 = range.map{ it.toDouble() / 100 }
    val y1 = range.map{ it.toDouble() / 100 }
    val z = mutableListOf<MutableList<Double>>()

    for (i in 0 until rangeSize) {
        z.add(MutableList(rangeSize){0.0})
    }

    for (i in 0 until rangeSize) {
        for (j in 0 until rangeSize) {
            val z1 = exp(-x1[i].pow(2) - y1[j].pow(2))
            val z2 = exp(-(x1[i] - 1).pow(2) - (y1[j] - 1).pow(2))
            z[i][j] = (z1 - z2) * 2
        }
    }

    val plot = Plotly.plot2D {
        contour {
            x.set(x1)
            y.set(y1)
            z(z)

            line {
                width = 4
            }

            colorscale = Value.of("Jet")
            contours {
                coloring = ContoursColoring.lines
                showlabels = true

                labelfont {
                    size = 16
                }
            }
        }

        layout {
            width = 700
            height = 600
            title { text = "Contour Lines with Labels"}
        }
    }

    plot.makeFile()
}