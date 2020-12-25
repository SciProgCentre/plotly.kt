import hep.dataforge.meta.invoke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.html.a
import kotlinx.html.h1
import kscience.plotly.Plotly
import kscience.plotly.models.Trace
import kscience.plotly.models.invoke
import kscience.plotly.plot
import kscience.plotly.server.close
import kscience.plotly.server.pushUpdates
import kscience.plotly.server.serve
import kscience.plotly.server.show
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun main() {

    val freq = 1.0 / 1000
    val oscillationFreq = 1.0 / 10000

    val x = (0..100).map { it.toDouble() / 100.0 }
    val sinY = x.map { sin(2.0 * PI * it) }
    val cosY = x.map { cos(2.0 * PI * it) }

    val sinTrace = Trace(x, sinY) { name = "sin" }
    val cosTrace = Trace(x, cosY) { name = "cos" }

    val server = Plotly.serve(port = 3872) {

        //root level plots go to default page
        page { plotly ->
            h1 { +"This is the plot page" }
            a("/other") { +"The other page" }
            plot(renderer = plotly) {
                traces(sinTrace, cosTrace)
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
            plot(renderer = plotly) {
                traces(sinTrace)
                layout {
                    title = "Dynamic plot"
                    xaxis.title = "x axis name"
                    yaxis.title = "y axis name"
                }
            }
        }

        pushUpdates(50)       // start sending updates via websocket to the front-end
    }

    server.show()

    //Start pushing updates
    GlobalScope.launch {
        var time: Long = 0

        while (isActive) {
            delay(10)
            time += 10
            sinTrace.y.numbers = x.map { sin(2.0 * PI * (it + time.toDouble() * freq)) }
            val cosAmp = cos(2.0 * PI * oscillationFreq * time)
            cosTrace.y.numbers = x.map { cos(2.0 * PI * (it + time.toDouble() * freq)) * cosAmp }
        }
    }

    println("Press Enter to close server")
    while (readLine()?.trim() != "exit"){
        //wait
    }

    server.close()
}