import kotlinx.html.h1
import kotlinx.html.hr
import scientifik.plotly.*
import scientifik.plotly.models.Trace
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import scientifik.plotly.models.invoke

@UnstablePlotlyAPI
fun main() {

    val x1 = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x1.map { sin(2.0 * PI * it) }
    val y2 = x1.map { cos(2.0 * PI * it) }

    val trace1 = Trace.invoke(x1, y1) { name = "sin" }
    val trace2 = Trace.invoke(x1, y2) { name = "cos" }

    Plotly.show {
        staticPlot {
            traces(trace1, trace2)
            layout {
                title { text = "The plot above" }
                xaxis { title { text = "x axis name" } }
                yaxis { title { text = "y axis name" } }
            }
        }
        hr()
        h1 { +"A custom separator" }
        hr()
        staticPlot{
            traces(trace1, trace2)
            layout {
                title { text = "The plot below" }
                xaxis { title { text = "x axis name" } }
                yaxis { title { text = "y axis name" } }
            }
        }
    }
}
