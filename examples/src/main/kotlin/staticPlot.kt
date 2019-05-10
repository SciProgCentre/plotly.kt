import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import kotlin.math.PI
import kotlin.math.sin

fun main() {
    val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
    val y = x.map { sin(2.0 * PI * it) }.toDoubleArray()

    Plotly.plot2D {
        trace(x, y) {
            name = "for a single trace in graph its name would be hidden"
        }

        layout {
            title = "Graph name"
            xaxis {
                title = "x axis"
            }
            yaxis {
                title = "y axis"
            }
        }
    }.makeFile()
}
