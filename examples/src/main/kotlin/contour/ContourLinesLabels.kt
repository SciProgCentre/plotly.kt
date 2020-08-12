package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.contour
import scientifik.plotly.makeFile
import scientifik.plotly.models.ContoursColoring
import kotlin.math.exp
import kotlin.math.pow

/**
 * - colored contour plot lines
 * - labels on contour lines
 * - change size of the plot and contour lines width
 */
fun main() {
    val range = -300..300
    val rangeSize = range.last - range.first + 1
    val x1 = range.map { it.toDouble() / 100 }
    val y1 = range.map { it.toDouble() / 100 }
    val values = mutableListOf<MutableList<Double>>()

    for (i in y1.indices) {
        values.add(MutableList(rangeSize) { 0.0 })
    }

    for (i in x1.indices) {
        for (j in y1.indices) {
            val z1 = exp(-x1[i].pow(2) - y1[j].pow(2))
            val z2 = exp(-(x1[i] - 1).pow(2) - (y1[j] - 1).pow(2))
            values[i][j] = (z1 - z2) * 2
        }
    }

    val plot = Plotly.plot {
        contour {
            x.set(x1)
            y.set(y1)
            z.set(values)

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
            title = "Contour Lines with Labels"
        }
    }

    plot.makeFile()
}