package pie

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Pie

/**
 * - basic pie(donut) chart
 * - change height and width of the plot
 */
fun main() {
    val values = listOf(19, 26, 55)
    val labels = listOf("Residential", "Non-Residential", "Utility")

    val pie = Pie {
        values(values)
        labels(labels)
        hole = 0.2
    }
    val plot = Plotly.plot2D {
        traces(pie)
        layout {
            width = 500
            height = 450
        }
    }

    plot.makeFile()
}