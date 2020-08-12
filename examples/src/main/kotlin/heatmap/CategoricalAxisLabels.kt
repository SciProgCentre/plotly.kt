package heatmap

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.heatmap
import scientifik.plotly.makeFile


/**
 * - Annotated heatmap with categorical labels
 * - change size of labels font
 * - rotate axis labels
 */
fun main() {
    val x1 = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val y1 = listOf("Morning", "Afternoon", "Evening")
    val z1 = listOf(
            listOf<Number?>(1, null, 30, 50, 1),
            listOf<Number>(20, 1, 60, 80, 30),
            listOf<Number>(30, 60, 1, -10, 20))

    val plot = Plotly.plot {
        heatmap {
            x.set(x1)
            y.set(y1)
            z.set(z1)
        }

        layout {
            xaxis {
                tickfont {
                    size = 16
                }
            }
            yaxis {
                tickangle = -90
                tickfont {
                    size = 16
                }
            }
            title {
                text = "Heatmap with Categorical Axis Labels"
                font { size = 20 }
            }
        }
    }

    plot.makeFile()
}