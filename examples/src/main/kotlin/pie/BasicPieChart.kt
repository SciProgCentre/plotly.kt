package pie

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Pie

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