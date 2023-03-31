package misc

import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.AxisSide
import space.kscience.plotly.scatter


fun main() {
    Plotly.plot {

        scatter {
            x(1, 2, 3)
            y(40, 50, 60)
            name = "yaxis data"
        }

        scatter {
            x(2, 3, 4)
            y(4, 5, 6)
            name = "yaxis2 data"
            yaxis = "y2"
        }

        layout {
            title = "Double Y Axis Example"
            yaxis {
                title = "yaxis title"
            }
            yaxis(2) {
                title {
                    text = "yaxis2 title"
                    font {
                        color("rgb(148, 103, 189)")
                    }
                }
                tickfont {
                    color("rgb(148, 103, 189)")
                }
                overlaying = "y"
                side = AxisSide.right
            }
        }
    }.makeFile()
}