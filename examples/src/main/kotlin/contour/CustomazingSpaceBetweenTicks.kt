package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Contour

fun main() {
    val z1 = listOf(
            listOf<Number>(10, 10.625, 12.5, 15.625, 20),
            listOf<Number>(5.625, 6.25, 8.125, 11.25, 15.625),
            listOf<Number>(2.5, 3.125, 5.0, 8.125, 12.5),
            listOf<Number>(0.625, 1.25, 3.125, 6.25, 10.625),
            listOf<Number>(0, 0.625, 2.5, 5.625, 10))

    val contour = Contour {
        z.set(z1)
        colorscale = Value.of("Jet")
        dx = 10
        x0 = Value.of(5)
        dy = 10
        y0 = Value.of(10)
    }

    val plot = Plotly.plot {
        traces(contour)

        layout {
            title = "Customizing Spacing Between X and Y Axis Ticks"
        }
    }

    plot.makeFile()
}