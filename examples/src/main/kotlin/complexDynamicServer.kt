import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.html.div
import kotlinx.html.link
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.models.Trace
import space.kscience.plotly.plot
import space.kscience.plotly.server.pushUpdates
import space.kscience.plotly.server.serve
import space.kscience.plotly.server.show
import space.kscience.plotly.trace
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
            while (true) {
                delay(40)
                val time = Instant.now()
                emit(sin(time.toEpochMilli().toDouble() / 2500))
            }
        }
        val cosFlow = flow {
            while (true) {
                delay(50)
                val time = Instant.now()
                emit(cos(time.toEpochMilli().toDouble() / 2500))
            }
        }
        val sinCosFlow = sinFlow.zip(cosFlow) { sin, cos ->
            sin to cos
        }
        page { renderer ->
            link {
                rel = "stylesheet"
                href = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
                attributes["integrity"] = "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
                attributes["crossorigin"] = "anonymous"
            }
            div("row") {
                div("col-6") {
                    plot(renderer = renderer) {
                        layout {
                            title = "sin property"
                            xaxis.title = "point index"
                            yaxis.title = "sin"
                        }
                        trace {
                            val flow: Flow<Iterable<Double>> = sinFlow.windowed(100)
                            launch {
                                updateFrom(Trace.Y_AXIS, flow)
                            }
                        }
                    }
                }
                div("col-6") {
                    plot(renderer = renderer) {
                        layout {
                            title = "cos property"
                            xaxis.title = "point index"
                            yaxis.title = "cos"
                        }
                        trace {
                            val flow: Flow<Iterable<Double>> = cosFlow.windowed(100)
                            launch {
                                updateFrom(Trace.Y_AXIS, flow)
                            }
                        }
                    }
                }
            }
            div("row") {
                div("col-12") {
                    plot(renderer = renderer) {
                        layout {
                            title = "cos vs sin"
                            xaxis.title = "sin"
                            yaxis.title = "cos"
                        }
                        trace {
                            name = "non-synchronized"
                            val flow: Flow<Iterable<Pair<Double, Double>>> = sinCosFlow.windowed(30)
                            launch {
                                updateXYFrom(flow)
                            }
                        }
                    }
                }
            }

        }
        pushUpdates(100) // start sending updates via websocket to the front-end
    }

    server.show()

    println("Press Enter to close server")
    readLine()

    server.stop(1000, 5000)
}