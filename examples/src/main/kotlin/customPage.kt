import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.hr
import scientifik.plotly.*
import scientifik.plotly.models.Trace
import scientifik.plotly.models.invoke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@UnstablePlotlyAPI
fun main() {

    val x1 = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x1.map { sin(2.0 * PI * it) }
    val y2 = x1.map { cos(2.0 * PI * it) }

    val trace1 = Trace(x1, y1) { name = "sin" }
    val trace2 = Trace(x1, y2) { name = "cos" }

    Plotly.show { container ->
        plot(handle = container) {
            traces(trace1, trace2)
            layout {
                title = "The plot above"
                xaxis.title = "x axis name"
                yaxis.title = "y axis name"
            }
        }
        hr()
        h1 { +"A custom separator" }
        hr()
        div {
            plot {
                traces(trace1, trace2)
                layout {
                    title = "The plot below"
                    xaxis.title = "x axis name"
                    yaxis.title = "y axis name"
                }
            }
        }
    }
}
