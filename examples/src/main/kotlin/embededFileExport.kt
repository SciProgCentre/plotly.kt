import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.ResourceLocation
import scientifik.plotly.makeFile
import scientifik.plotly.trace
import kotlin.math.PI
import kotlin.math.sin

fun main() {
    val x = (0..100).map { it.toDouble() / 100.0 }
    val y = x.map { sin(2.0 * PI * it) }

    val plot = Plotly.plot2D {
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
    }

    plot.makeFile(resourceLocation = ResourceLocation.EMBED)
}