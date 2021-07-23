package scatter

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.models.TextPosition
import space.kscience.plotly.scatter


/**
 * - Scatter plot only with markers
 * - Data labels on the plot
 * - Change textfont family
 */
fun main() {
    val plot = Plotly.plot {
        scatter {
            x(1, 2, 3, 4)
            y(10, 15, 13, 17)
            mode = ScatterMode.markers
            name = "Team A"
            text("A-1", "A-2", "A-3", "A-4", "A-5")
            textposition = TextPosition.`top center`
            textfont {
                family = "Raleway, sans-serif"
            }
            marker { size = 12 }
        }

        scatter {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = ScatterMode.lines
            name = "Team B"
            text("B-a", "B-b", "B-c", "B-d", "B-e")
            textposition = TextPosition.`bottom center`
            textfont {
                family = "Times New Roman"
            }
            marker { size = 12 }
        }

        layout {
            title = "Data Labels Hover"
            xaxis {
                range(0.75..5.25)
            }
            legend {
                y = 0.5
                font {
                    family = "Arial, sans-serif"
                    size = 20
                    color("grey")
                }
            }
        }
    }

    plot.makeFile()
}
