package pie

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.pie

/**
 * - basic pie(donut) chart
 * - change height and width of the plot
 */
fun main() {
    val values = listOf(19, 26, 55)
    val labels = listOf("Residential", "Non-Residential", "Utility")

    val plot = Plotly.plot2D {
        pie {
            values(values)
            labels(labels)
            hole = 0.2

        }

        layout {
            width = 500
            height = 450
        }
    }

    plot.makeFile()
}