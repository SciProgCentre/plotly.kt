package scatter

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.ScatterMode
import kscience.plotly.models.TextPosition
import kscience.plotly.scatter


fun main() {
    val plot = Plotly.plot {
        scatter {
            x(1, 2, 3, 4)
            y(10, 15, 13, 17)
            mode = ScatterMode.markers
            name = "Team A"
            textsList = listOf("A-1", "A-2", "A-3", "A-4", "A-5")
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
            textsList = listOf("B-a", "B-b", "B-c", "B-d", "B-e")
            textposition = TextPosition.`bottom center`
            textfont {
                family = "Times New Roman"
            }
            marker { size = 12 }
        }

        layout {
            title = "Data Labels Hover"
            xaxis {
                range = 0.75..5.25
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
