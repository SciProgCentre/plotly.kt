package contour

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Contour

fun main() {
    val z1 = listOf(10, 10.625, 12.5, 15.625, 20,
                    5.625, 6.25, 8.125, 11.25, 15.625,
                    2.5, 3.125, 5.0, 8.125, 12.5,
                    0.625, 1.25, 3.125, 6.25, 10.625,
                    0, 0.625, 2.5, 5.625, 10).chunked(5)

    val contour = Contour {
        z(z1)
        colorscale = Value.of("Jet")
        dx = 10
        x0 = Value.of(5)
        dy = 10
        y0 = Value.of(10)
    }

    val plot = Plotly.plot2D {
        traces(contour)

        layout {
            title { text = "Customizing Spacing Between X and Y Axis Ticks" }
        }
    }

    plot.makeFile()
}