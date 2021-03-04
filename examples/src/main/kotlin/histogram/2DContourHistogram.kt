package histogram

import space.kscience.dataforge.values.Value
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ContoursColoring
import space.kscience.plotly.models.Histogram2DContour
import space.kscience.plotly.models.MeasureMode


/**
 * - 2D contour histogram of a bivariate distribution
 * - change histogram colorscale
 * - change colorbar thickness and thickness mode
 * - change width of the colorbar border
 * - change contours coloring
 */
fun main() {
    val x1 = mutableListOf<Double>()
    val y1 = mutableListOf<Double>()

    for (i in 0 until 500) {
        x1.add(Math.random())
        y1.add(Math.random())
    }

    val trace = Histogram2DContour {
        x.set(x1)
        y.set(y1)
        colorscale = Value.of("Greens")
        contours {
            coloring = ContoursColoring.heatmap
        }
        colorbar {
            borderwidth = 0
            thicknessmode = MeasureMode.fraction
            thickness = 0.05
        }
    }

    val plot = Plotly.plot {
        traces(trace)

        layout {
            title = "2D Contour Histogram of a Bivariate Normal Distribution"
        }
    }

    plot.makeFile()
}