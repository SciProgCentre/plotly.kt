package bar

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Bar
import scientifik.plotly.models.BarMode
import scientifik.plotly.palettes.XKCD


/**
 * - Grouped bar chart
 * - Use XKCD color palette
 */
fun main() {
    val trace1 = Bar {
        x("giraffes", "orangutans", "monkeys")
        y(20, 14, 23)
        name = "SF Zoo"
        marker {
            color(XKCD.GREEN)
        }
    }

    val trace2 = Bar {
        x("giraffes", "orangutans", "monkeys")
        y(12, 18, 29)
        name = "LA Zoo"
        marker {
            color(XKCD.BLUE)
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "Grouped Bar Chart"
            barmode = BarMode.group
        }
    }
    plot.makeFile()
}