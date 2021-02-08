import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.UnstablePlotlyAPI
import kscience.plotly.makeFile
import kscience.plotly.models.functionXY
import kscience.plotly.trace
import kotlin.math.PI
import kotlin.math.sin

@OptIn(UnstablePlotlyAPI::class)
fun main() {
    val plot = Plotly.plot {
        repeat(50) { phase ->
            trace {
                functionXY(0.0..2 * PI, step = 0.05) {
                    sin(it + phase * 2 * PI / 60)
                }
                name = "Sin with phase offset ${phase * 2 * PI / 60}"
            }
        }

        layout {
            title = "Graph name"
            xaxis {
                title = "x axis"
            }
            yaxis {
                title = "y axis"
            }
            height = 700
        }
    }

    plot.makeFile()
}
