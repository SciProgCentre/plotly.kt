import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import scientifik.plotly.Plotly
import scientifik.plotly.models.Trace
import scientifik.plotly.server.PlotlyServer
import scientifik.plotly.server.serve
import kotlin.math.PI
import kotlin.math.sin

fun main() {

    val server = Plotly.serve{
        // start sending updates via websocket to the front-end
        updateMode = PlotlyServer.UpdateMode.PUSH
        updateInterval = 50

        val x = (0..100).map { it.toDouble() / 100.0 }
        val y = x.map { sin(2.0 * PI * it) }

        val trace = Trace.build(x = x, y = y) { name = "sin" }


        //root level plots go to default page

        plot {
            trace(trace)
            layout {
                title = "Dynamic plot"
                xaxis { title = "x axis name" }
                yaxis { title = "y axis name" }
            }
        }

        launch {
            var time: Long = 0
            while (isActive) {
                delay(10)
                time += 10
                val dynamicY = x.map { sin(2.0 * PI * (it + time.toDouble() / 1000.0)) }
                trace.y(dynamicY)
            }
        }
    }

    println("Press Enter to close server")
    readLine()

    server.stop()
}