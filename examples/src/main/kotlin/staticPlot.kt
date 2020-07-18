import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.trace
import kotlin.math.PI
import kotlin.math.sin

fun main() {
    val x = (0..100).map { it.toDouble() / 100.0 }
    val y = x.map { sin(2.0 * PI * it) }

    val plot = Plotly.plot {
        trace(x, y) {
            name = "for a single trace in graph its name would be hidden"
        }
        layout {
            title { text = "Graph name" }
            xaxis {
                title { text = "x axis" }
            }
            yaxis {
                title { text = "y axis" }
            }
        }
    }

    plot.makeFile()
}
