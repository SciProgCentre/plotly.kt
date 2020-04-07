import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Trace
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() {

    val x = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x.map { sin(2.0 * PI * it) }
    val y2 = x.map { cos(2.0 * PI * it) }

    val trace1 = Trace.build(x, y1) { name = "sin" }
    val trace2 = Trace.build(x, y2) { name = "cos" }


    val plot = Plotly.page {
        title = "Page sample"
        plot(1, 8) {
            addTrace(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis { title = "x axis name" }
                yaxis { title = "y axis name" }
            }
        }

        plot(1, 4) {
            addTrace(trace1, trace2)
            layout {
                title = "Second graph, row: 1, size: 4/12"
                xaxis { title = "x axis name" }
                yaxis { title = "y axis name" }
            }
        }

        plot(2, 12) {
            addTrace(trace1, trace2)
            layout {
                title = "Third graph, row: 2, size: 12/12"
                xaxis { title = "x axis name" }
                yaxis { title = "y axis name" }
            }
        }
    }

    plot.makeFile()
}
