package bar

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Bar


/**
 * - Bar chart with customized base
 * -
 */
fun main() {
    val trace1 = Bar {
        x("2016", "2017", "2018")
        y(500, 600, 700)
        base = listOf(-500, -600, -700)
        marker {
            color("red")
        }
        name = "expenses"
    }

    val trace2 = Bar {
        x("2016", "2017", "2018")
        y(300, 400, 700)
        marker {
            color("blue")
        }
        name = "revenue"
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "Customizing individual bar base"
        }
    }
    plot.makeFile()
}