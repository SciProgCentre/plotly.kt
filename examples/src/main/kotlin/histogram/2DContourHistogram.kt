package histogram

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Histogram2DContour


/**
 * - 2D contour histogram of a bivariate distribution
 * - change histogram colorscale
 */
fun main() {
    val x = mutableListOf<Double>()
    val y = mutableListOf<Double>()

    for (i in 0 until 500) {
        x.add(Math.random())
        y.add(Math.random() + 1)
    }

    val trace = Histogram2DContour(x, y) {
        colorscale = Value.of("Greens")
    }

    val plot = Plotly.plot2D {
        traces(trace)

        layout {
            title {
                text = "2D Contour Histogram of a Bivariate Normal Distribution"
            }
        }
    }

    plot.makeFile()
}