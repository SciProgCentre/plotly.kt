package pie

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Pie

/**
 * - basic pie chart
 * - change height and width of the plot
 */
fun main() {
    val values = listOf(19, 26, 55)
    val labels = listOf("Residential", "Non-Residential", "Utility")

    val pie = Pie {
        values(values)
        labels(labels)
    }

    val plot = Plotly.plot {
        traces(pie)
        layout {
            width = 500
            height = 450
        }
    }

    plot.makeFile()
}