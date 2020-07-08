package pie

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Trace
import scientifik.plotly.models.TraceType


/**
 * - basic pie chart
 * - change height and width of the plot
 */
fun main() {
    val values = listOf(19, 26, 55)
    val labels = listOf("Residential", "Non-Residential", "Utility")

    val trace = Trace {
        type = TraceType.pie
        values(values)
        labels(labels)
    }

    val plot = Plotly.plot2D {
        addTrace(trace)
        layout {
            width = 500
            height = 450
        }
    }

    plot.makeFile()
}