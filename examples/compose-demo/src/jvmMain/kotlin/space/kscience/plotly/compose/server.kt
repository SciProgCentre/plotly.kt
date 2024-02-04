package space.kscience.plotly.compose

import io.ktor.server.engine.ApplicationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import space.kscience.plotly.*
import space.kscience.plotly.models.Scatter
import space.kscience.plotly.models.invoke
import space.kscience.plotly.server.pushUpdates
import space.kscience.plotly.server.serve
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun staticPlot(): String = Plotly.page {
    val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
    val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
    val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()
    val trace1 = Scatter(x, y1) {
        name = "sin"
    }
    val trace2 = Scatter(x, y2) {
        name = "cos"
    }
    plot(config = PlotlyConfig { responsive = true }) {//static plot
        traces(trace1, trace2)
        layout {
            title = "First graph, row: 1, size: 8/12"
            xaxis.title = "x axis name"
            yaxis { title = "y axis name" }
        }
    }
}.render()

fun CoroutineScope.servePlots(scale: StateFlow<Number>): ApplicationEngine = Plotly.serve(this, port = 7778) {
    page("Static") { container ->
        val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()
        val trace1 = Scatter(x, y1) {
            name = "sin"
        }
        val trace2 = Scatter(x, y2) {
            name = "cos"
        }
        plot(renderer = container) {//static plot
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

        val trace = Scatter(x, y) { name = "sin" }

        val plot = plot("dynamic", config = PlotlyConfig { responsive = true }, renderer = container) {
            traces(trace)
            layout {
                title = "Dynamic plot"
                xaxis.title = "x axis name"
                yaxis.title = "y axis name"
            }
        }

        launch {
            var time: Long = 0
            while (isActive) {
                delay(10)
                time += 10
                val frequency = scale.value.toDouble()
                val dynamicY = x.map { sin(2.0 * PI * frequency * (it + time.toDouble() / 1000.0)) }
                //trace.y.numbers = dynamicY
                plot.data[0].y.numbers = dynamicY
                plot.layout {
                    xaxis.title = "x axis name (t = $time)"
                }
            }
        }
    }
    pushUpdates(100)
}


