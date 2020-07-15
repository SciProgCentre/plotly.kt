package scatter

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.models.TextPosition
import scientifik.plotly.scatter

fun main() {
    val plot = Plotly.plot2D {
        scatter {
            x(1, 2, 3, 4)
            y(10, 15, 13, 17)
            mode = ScatterMode.markers
            name = "Team A"
            text = listOf("A-1", "A-2", "A-3", "A-4", "A-5")
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
            text = listOf("B-a", "B-b", "B-c", "B-d", "B-e")
            textposition = TextPosition.`bottom center`
            textfont {
                family = "Times New Roman"
            }
            marker { size = 12 }
        }

        layout {
            title {
                text = "Data Labels Hover"
            }
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
