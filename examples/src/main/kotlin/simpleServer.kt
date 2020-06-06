import hep.dataforge.meta.invoke
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import scientifik.plotly.Plotly
import scientifik.plotly.models.Trace
import scientifik.plotly.server.serve
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
fun main() {
    val server = Plotly.serve {
        val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()

        val trace1 = Trace(x, y1) { name = "sin" }
        val trace2 = Trace(x, y2) { name = "cos" }


        //root level plots go to default page

        plot(1, 8) {
            addTrace(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis { title = "x axis name" }
                yaxis { title = "y axis name" }
            }
        }

        val plot1 = plot(1, 4) {
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

        page("other") {
            title = "Other page"
            plot(plot1, id = "plot1")
        }

    }

    println("Press Enter to close server")
    readLine()

    server.stop()

}