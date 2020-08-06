package scatter

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Dash
import scientifik.plotly.models.Scatter
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.models.ShapeType
import scientifik.plotly.shape


/**
 * - Text scatter mode
 * - Add different lines using shapes
 */
fun main() {
    val trace1 = Scatter {
        x(2, 3.5, 6)
        y(1, 1.5, 1)
        textsList = listOf("Vertical Line", "Horizontal Dashed Line", "Diagonal dotted Line")
        mode = ScatterMode.text
    }

    val plot = Plotly.plot {
        traces(trace1)

        layout {
            title = "Vertical and Horizontal Lines Positioned Relative to the Axes"
            xaxis {
                range = 0.0..7.0
            }
            yaxis {
                range = 0.0..2.5
            }

            width = 700
            height = 500

            // vertical line
            shape {
                type = ShapeType.line
                x0 = Value.of(1)
                y0 = Value.of(0)
                x1 = Value.of(1)
                y1 = Value.of(2)
                line {
                    color("rgb(55, 128, 191")
                    width = 3
                }
            }

            // horizontal line
            shape {
                type = ShapeType.line
                x0 = Value.of(2)
                y0 = Value.of(2)
                x1 = Value.of(5)
                y1 = Value.of(2)
                line {
                    color("rgb(50, 171, 96)")
                    width = 4
                    dash = Dash.dashdot
                }
            }

            // diagonal line
            shape {
                type = ShapeType.line
                x0 = Value.of(4)
                y0 = Value.of(0)
                x1 = Value.of(6)
                y1 = Value.of(2)
                line {
                    color("rgb(128, 0, 128)")
                    width = 4
                    dash = Dash.dot
                }
            }
        }
    }
    plot.makeFile()
}