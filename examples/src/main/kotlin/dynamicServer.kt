import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.html.a
import kotlinx.html.h1
import scientifik.plotly.Plotly
import scientifik.plotly.layout
import scientifik.plotly.models.Trace
import scientifik.plotly.server.pushUpdates
import scientifik.plotly.server.serve
import scientifik.plotly.server.servePlot
import kotlin.math.PI
import kotlin.math.sin

fun main() {

    val server = Plotly.serve {

        val x = (0..100).map { it.toDouble() / 100.0 }
        val y = x.map { sin(2.0 * PI * it) }

        val trace = Trace(x, y) { name = "sin" }


        //root level plots go to default page
        page { container ->
            h1{+"This is the plot page"}
            a("/other"){ +"The other page"}
            servePlot(container) {
                traces(trace)
                layout {
                    title { text = "Dynamic plot" }
                    xaxis { title { text = "x axis name" } }
                    yaxis { title { text = "y axis name" } }
                }
            }
        }

        page("other") { container ->
            h1 { +"This is the other plot page" }
            a("/"){ +"Back to the main page"}
            servePlot(container) {
                traces(trace)
                layout {
                    title { text = "Dynamic plot" }
                    xaxis { title { text = "x axis name" } }
                    yaxis { title {text = "y axis name" } }
                }
            }
        }

        GlobalScope.launch {
            var time: Long = 0
            while (isActive) {
                delay(10)
                time += 10
                val dynamicY = x.map { sin(2.0 * PI * (it + time.toDouble() / 1000.0)) }
                trace.y.set(dynamicY)
            }
        }
    }.pushUpdates(50)        // start sending updates via websocket to the front-end

    server.show()

    println("Press Enter to close server")
    readLine()

    server.close()
}