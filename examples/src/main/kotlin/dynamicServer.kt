import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.html.a
import kotlinx.html.h1
import kscience.plotly.Plotly
import kscience.plotly.layout
import kscience.plotly.models.Trace
import kscience.plotly.models.invoke
import kscience.plotly.plot
import kscience.plotly.server.close
import kscience.plotly.server.pushUpdates
import kscience.plotly.server.serve
import kscience.plotly.server.show
import kotlin.math.PI
import kotlin.math.sin


fun main() {

    val server = Plotly.serve(port = 3872) {

        val x = (0..100).map { it.toDouble() / 100.0 }
        val y = x.map { sin(2.0 * PI * it) }

        val trace = Trace.invoke(x, y) { name = "sin" }


        //root level plots go to default page
        page { plotly ->
            h1 { +"This is the plot page" }
            a("/other") { +"The other page" }
            plot(container = plotly) {
                traces(trace)
                layout {
                    title = "Other dynamic plot"
                    xaxis.title = "x axis name"
                    yaxis.title = "y axis name"
                }
            }
        }

        page("other") { plotly ->
            h1 { +"This is the other plot page" }
            a("/") { +"Back to the main page" }
            plot(container = plotly) {
                traces(trace)
                layout {
                    title = "Dynamic plot"
                    xaxis.title = "x axis name"
                    yaxis.title = "y axis name"
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
        pushUpdates(50)       // start sending updates via websocket to the front-end
    }

    server.show()

    println("Press Enter to close server")
    readLine()

    server.close()
}