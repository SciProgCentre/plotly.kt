package box

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Box
import scientifik.plotly.models.BoxPoints
import scientifik.plotly.models.Trace
import kotlin.random.Random


fun getRandom(size: Int, mul: Double): List<Double> {
    val value = mutableListOf<Double>()
    for (i in 0..size) {
        value.add(Random.nextDouble() * mul)
    }
    return value.toList()
}

/**
 * - Styled box plot
 * - Style zeroline & grid
 * - Change background color of the plot
 * - Use array of traces
 */
fun main() {
    val xData = listOf("Carmelo<br>Anthony", "Dwyane<br>Wade", "Deron<br>Williams",
            "Brook<br>Lopez", "Damian<br>Lillard", "David<br>West",
            "Blake<br>Griffin", "David<br>Lee", "Demar<br>Derozan")
    val yData = listOf(
            getRandom(30, 10.0),
            getRandom(30, 20.0),
            getRandom(30, 25.0),
            getRandom(30, 40.0),
            getRandom(30, 45.0),
            getRandom(30, 30.0),
            getRandom(30, 20.0),
            getRandom(30, 15.0),
            getRandom(30, 43.0),
            getRandom(30, 10.0)
    )

    val data = mutableListOf<Trace>()
    for (i in xData.indices) {
        val result = Box {
            y.set(yData[i])
            name = xData[i]
            boxpoints = BoxPoints.all
            jitter = 0.5
            whiskerwidth = 0.2
            fillcolor("cls")
            marker {
                size = 2
            }
            line {
               width = 1
            }
            showlegend = false
        }
        data.add(result)
    }

    val plot = Plotly.plot {
        traces(data)

        layout {
            title = "Points Scored by the Top 9 Scoring NBA Players in 2012"
            yaxis {
                autorange = true
                showgrid = true
                zeroline = true
                gridcolor("rgb(255, 255, 255)")
                gridwidth = 1
                zerolinecolor("rgb(255, 255, 255)")
                zerolinewidth = 2
            }
            margin {
                l = 40
                r = 30
                b = 80
                t = 100
            }
            paper_bgcolor("rgb(243, 243, 243)")
            plot_bgcolor("rgb(243, 243, 243)")
        }
    }
    plot.makeFile()
}