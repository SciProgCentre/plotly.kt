package kscience.plotly.fx

import javafx.beans.value.ObservableIntegerValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kscience.plotly.Plotly
import kscience.plotly.layout
import kscience.plotly.models.Trace
import kscience.plotly.models.invoke
import kscience.plotly.plot
import kscience.plotly.server.pushUpdates
import kscience.plotly.server.serve
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun serve(scale: ObservableIntegerValue) = Plotly.serve {
    embedData = true //Should be set this way to avid FX browser bug

    page("Static") {
        val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()
        val trace1 = Trace(x, y1) {
            name = "sin"
        }
        val trace2 = Trace(x, y2) {
            name = "cos"
        }
        plot {//static plot
            traces(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis.title = "x axis name"
                yaxis { title = "y axis name" }
            }
        }
    }

    page("Dynamic") { container ->
        val x = (0..100).map { it.toDouble() / 100.0 }
        val y = x.map { sin(2.0 * PI * it) }

        val trace = Trace(x, y) { name = "sin" }

        plot("dynamic", renderer = container) {
            traces(trace)
            layout {
                title = "Dynamic plot"
                xaxis.title = "x axis name"
                yaxis.title = "y axis name"
            }
        }

        GlobalScope.launch {
            var time: Long = 0
            while (isActive) {
                delay(10)
                time += 10
                val frequency = scale.get().toDouble()
                val dynamicY = x.map { sin(2.0 * PI * frequency * (it + time.toDouble() / 1000.0)) }
                trace.y.numbers = dynamicY
            }
        }
    }
    pushUpdates(100)
}


