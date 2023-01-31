package contour

import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.contour
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ContoursColoring
import space.kscience.plotly.models.Dash
import kotlin.math.exp
import kotlin.math.pow

/**
 * - colored contour plot lines
 * - labels on contour lines
 * - hide colorbar
 * - change size of the plot and contour lines width
 * - change style of contour lines
 */
fun main() {
    val range = -160..260 step 4
    val rangeSize = (range.last - range.first) / 4 + 1
    val x1 = range.map { it.toDouble() / 100 }
    val y1 = range.map { it.toDouble() / 100 }
    val z1 = mutableListOf<MutableList<Double?>>()
    val z2 = mutableListOf<MutableList<Double?>>()

    for (i in x1.indices) {
        z1.add(MutableList(rangeSize) { 0.0 })
        z2.add(MutableList(rangeSize) { 0.0 })
    }

    for (i in x1.indices) {
        for (j in y1.indices) {
            val elem1 = exp(-x1[i].pow(2) - y1[j].pow(2))
            val elem2 = exp(-(x1[i] - 1).pow(2) - (y1[j] - 1).pow(2))
            val elem = (elem1 - elem2) * 2
            if (elem >= -0.01) {
                z1[i][j] = elem
                z2[i][j] = null
            } else {
                z2[i][j] = elem
                z1[i][j] = null
            }
        }
    }

    val plot = Plotly.plot {
        contour {
            x.set(x1)
            y.set(y1)
            z.set(z1)

            line {
                width = 4
            }
            connectgaps = true
            colorscale = Value.of("Jet")
            showscale = false
            contours {
                coloring = ContoursColoring.lines
                showlabels = true
            }
        }

        contour {
            x.set(x1)
            y.set(y1)
            z.set(z2)

            line {
                width = 4
                dash = Dash.dash
            }
            connectgaps = true
            colorscale = Value.of("Jet")
            reversescale = true
            showscale = false
            contours {
                coloring = ContoursColoring.lines
                showlabels = true
            }
        }

        layout {
            width = 900
            height = 750
            title = "Negative Contours Dashed"
        }
    }

    plot.makeFile()
}