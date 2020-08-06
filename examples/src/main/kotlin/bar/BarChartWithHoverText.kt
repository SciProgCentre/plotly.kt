package bar

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.bar
import scientifik.plotly.makeFile


/**
 * - Bar chart with hover text
 * - Use rgb color palette
 * - Rotate axis ticks
 */
fun main() {
    val plot = Plotly.plot {
        bar {
            x("Liam", "Sophie", "Jacob", "Mia", "William", "Olivia")
            y(8.0, 8.0, 12.0, 12.0, 13.0, 20.0)
            textsList = listOf("4.17 below the mean", "4.17 below the mean", "0.17 below the mean",
                    "0.17 below the mean", "0.83 above the mean", "7.83 above the mean")
            marker {
                color("rgb(142, 124, 195)")
            }
            showlegend = false
        }

        layout {
            title {
                text = "Number of Graphs Made this Week"
                font {
                    family = "Raleway, sans-serif"
                }
            }
            xaxis {
                tickangle = -45
            }
            yaxis {
                zeroline = false
                gridwidth = 2
            }
            bargap = 0.05
        }
    }
    plot.makeFile()
}