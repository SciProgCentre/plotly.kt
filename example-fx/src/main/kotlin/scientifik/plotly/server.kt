package scientifik.plotly

import javafx.beans.value.ObservableIntegerValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import scientifik.plotly.models.Trace
import scientifik.plotly.server.PlotlyServer
import scientifik.plotly.server.pullUpdates
import scientifik.plotly.server.serve
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun serve(scale: ObservableIntegerValue): PlotlyServer = Plotly.serve {

    page("Static") {
        val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()
        val trace1 = Trace.build(x = x, y = y1) { name = "sin" }
        val trace2 = Trace.build(x = x, y = y2) { name = "cos" }
        plot {
            trace(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis { title = "x axis name" }
                yaxis { title = "y axis name" }
            }
        }
    }

    page("Dynamic") {
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
                val frequency = scale.get().toDouble()
                val dynamicY = x.map { sin(2.0 * PI * frequency * (it + time.toDouble() / 1000.0)) }
                trace.y = dynamicY
            }
        }
    }
}.pullUpdates(300)


