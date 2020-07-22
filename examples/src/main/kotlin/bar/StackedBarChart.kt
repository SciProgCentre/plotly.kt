package bar

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.bar
import scientifik.plotly.makeFile
import scientifik.plotly.models.BarMode
import scientifik.plotly.models.XAnchor
import scientifik.plotly.models.YAnchor


/**
 * - Stacked bar chart
 * - Use css named colors
 * - Show legend inside the plot
 * - Change margins
 */
fun main() {
    val values = listOf<Number>(1, 2, 3.5, 4.1, 4.7, 4, 3.2, 1.95, 1)

    val plot = Plotly.plot {
        bar {
            y.set(values)
            marker {
                color("red")
            }
        }

        bar {
            y.set(values)
            marker {
                color("orange")
            }
        }

        bar {
            y.set(values)
            marker {
                color("yellow")
            }
        }

        bar {
            y.set(values)
            marker {
                color("greenyellow")
            }
        }

        bar {
            y.set(values)
            marker {
                color("limegreen")
            }
        }

        layout {
            barmode = BarMode.stack
            title = "Stacked Bar Chart"
            margin { l = 20; r = 20; b = 20; t = 40 }

            legend {
                xanchor = XAnchor.right
                yanchor = YAnchor.top
                x = 1
                y = 1
                borderwidth = 1
            }
        }
    }
    plot.makeFile()
}