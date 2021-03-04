package bar

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.bar
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.BarMode
import space.kscience.plotly.models.XAnchor
import space.kscience.plotly.models.YAnchor


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
            y.numbers = values
            marker { color("red") }
        }

        bar {
            y.numbers = values
            marker { color("orange") }
        }

        bar {
            y.numbers = values
            marker { color("yellow") }
        }

        bar {
            y.numbers = values
            marker { color("greenyellow") }
        }

        bar {
            y.numbers = values
            marker { color("limegreen") }
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