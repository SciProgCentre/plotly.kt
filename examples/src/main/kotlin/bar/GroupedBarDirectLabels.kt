package bar

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Bar
import scientifik.plotly.models.TextPosition


/**
 * - Grouped bar chart
 * - Use rgb(a) color palette
 * - Hide hoverinfo & legend
 * - Show text on the bars
 */
fun main() {
    val xValue = listOf("Product A", "Product B", "Product C")

    val yValue = listOf(20, 14, 23)
    val yValue2 = listOf(24, 16, 20)

    val trace1 = Bar {
        x.set(xValue)
        y.set(yValue)
        text = yValue.map{ it.toString() }
        textposition = TextPosition.auto
        hoverinfo = "none"
        opacity = 0.5
        showlegend = false
        marker {
            color("rgb(158, 202, 225)")
            line {
                color("rgb(8, 48, 107)")
                width = 2
            }
        }
    }

    val trace2 = Bar {
        x.set(xValue)
        y.set(yValue2)
        text = yValue2.map{ it.toString() }
        textposition = TextPosition.auto
        hoverinfo = "none"
        showlegend = false
        marker {
            color("rgba(58, 200, 225, 0.5)")
            line {
                color("rgb(8, 48, 107)")
                width = 2
            }
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "January 2013 Sales Report"
        }
    }
    plot.makeFile()
}