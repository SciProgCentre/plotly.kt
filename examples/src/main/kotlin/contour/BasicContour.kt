package contour

import scientifik.plotly.Plotly
import scientifik.plotly.layout
import scientifik.plotly.makeFile
import scientifik.plotly.models.Contour
import scientifik.plotly.models.MeasureMode


/**
 * - basic contour plot
 * - change font size of labels ticks
 * - setting x and y coordinates
 * - change color bar size
 */
fun main() {
    val values: List<List<Number>> = listOf(
        listOf<Number>(10, 10.625, 12.5, 15.625, 20.0),
        listOf<Number>(5.625, 6.25, 8.125, 11.25, 15.625),
        listOf<Number>(2.5, 3.125, 5.0, 8.125, 12.5),
        listOf<Number>(0.625, 1.25, 3.125, 6.25, 10.625),
        listOf<Number>(0.0, 0.625, 2.5, 5.625, 10)
    )
    val x1 = listOf(-9, -6, -5 , -3, -1)
    val y1 = listOf(0, 1, 4, 5, 7)

    val contour = Contour {
        x.numbers = x1
        y.numbers = y1
        z(values)

        colorbar {
            thickness = 75.0
            thicknessmode = MeasureMode.pixels
            len = 0.9
            lenmode = MeasureMode.fraction
            outlinewidth = 0
        }
    }

    val plot = Plotly.plot {
        traces(contour)

        layout {
            title  = "Basic Contour Plot"
            xaxis {
                tickfont {
                    size = 16
                }
            }
            yaxis {
                tickfont {
                    size = 16
                }
            }
        }
    }

    plot.makeFile()
}