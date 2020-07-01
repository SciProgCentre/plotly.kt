package scatter

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TextPosition
import scientifik.plotly.models.TraceMode
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace

fun main() {
    val plot = Plotly.plot2D {
        trace {
            x (1, 2, 3, 4)
            y (10, 15, 13, 17)
            mode = TraceMode.markers
            type = TraceType.scatter
            name = "Team A"
            text = listOf("A-1", "A-2", "A-3", "A-4", "A-5")
            textposition = TextPosition.topCenter
            textfont {
                family = "Raleway, sans-serif"
            }
            marker { size = 12 }
        }
        trace {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = TraceMode.lines
            type = TraceType.scatter
            name = "Team B"
            text = listOf("B-a", "B-b", "B-c", "B-d", "B-e")
            textposition = TextPosition.bottomCenter
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
                    color = "grey"
                }
            }
        }
    }

    plot.makeFile()
}
