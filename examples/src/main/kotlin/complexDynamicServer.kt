import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.html.div
import kotlinx.html.link
import scientifik.plotly.Plotly
import scientifik.plotly.layout
import scientifik.plotly.models.Trace
import scientifik.plotly.plot
import scientifik.plotly.server.pushUpdates
import scientifik.plotly.server.serve
import scientifik.plotly.trace
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.cos
import kotlin.math.sin


/**
 * In-place replacement for absent method from stdlib
 */
fun <T> Flow<T>.windowed(size: Int): Flow<Iterable<T>> {
    val queue = ConcurrentLinkedQueue<T>()
    return flow {
        this@windowed.collect {
            queue.add(it)
            if (queue.size >= size) {
                queue.poll()
            }
            emit(queue)
        }
    }
}

suspend fun Trace.updateFrom(axisName: String, flow: Flow<Iterable<Double>>) {
    flow.collect {
        axis(axisName).numbers = it
    }
}

suspend fun Trace.updateXYFrom(flow: Flow<Iterable<Pair<Double, Double>>>) {
    flow.collect { pairs ->
        x.numbers = pairs.map { it.first }
        y.numbers = pairs.map { it.second }
    }
}


fun main() {
    val server = Plotly.serve {
        val sinFlow = flow {
            while(true){
                delay(200)
                val time = Instant.now()
                emit(sin(time.toEpochMilli().toDouble() / 5000))
            }
        }
        val cosFlow = flow {
            while(true){
                delay(250)
                val time = Instant.now()
                emit(cos(time.toEpochMilli().toDouble() / 5000))
            }
        }
        val sinCosFlow = sinFlow.zip(cosFlow) { sin, cos ->
            sin to cos
        }
        page { container ->
            link {
                rel = "stylesheet"
                href = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
                attributes["integrity"] = "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
                attributes["crossorigin"] = "anonymous"
            }
            div("row") {
                div("col-6") {
                    plot(container = container) {
                        layout {
                            title = "sin property"
                            xaxis.title = "point index"
                            yaxis.title = "sin"
                        }
                        trace {
                            GlobalScope.launch {
                                val flow: Flow<Iterable<Double>> = sinFlow.windowed(100)
                                updateFrom(Trace.Y_AXIS, flow)
                            }
                        }
                    }
                }
                div("col-6") {
                    plot(container = container) {
                        layout {
                            title = "cos property"
                            xaxis.title = "point index"
                            yaxis.title = "cos"
                        }
                        trace {
                            GlobalScope.launch {
                                val flow: Flow<Iterable<Double>> = cosFlow.windowed(100)
                                updateFrom(Trace.Y_AXIS, flow)
                            }
                        }
                    }
                }
            }
            div("row") {
                div("col-12") {
                    plot(container = container) {
                        layout {
                            title = "cos vs sin"
                            xaxis.title = "sin"
                            yaxis.title = "cos"
                        }
                        trace {
                            name = "non-synchronized"
                            GlobalScope.launch {
                                val flow: Flow<Iterable<Pair<Double, Double>>> = sinCosFlow.windowed(30)
                                updateXYFrom(flow)
                            }
                        }
                    }
                }
            }

        }
    }.pushUpdates(50)        // start sending updates via websocket to the front-end

    server.show()

    println("Press Enter to close server")
    readLine()

    server.close()
}