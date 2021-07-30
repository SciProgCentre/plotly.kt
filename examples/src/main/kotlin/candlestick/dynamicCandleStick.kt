package candlestick

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.models.AxisType
import space.kscience.plotly.models.DragMode
import space.kscience.plotly.plot
import space.kscience.plotly.server.close
import space.kscience.plotly.server.pushUpdates
import space.kscience.plotly.server.serve
import space.kscience.plotly.server.show
import kotlin.random.Random

fun main() {
    val server = Plotly.serve {
        pushUpdates(50)
        page { plotly ->
            plot(renderer = plotly) {
                traces(candleStickTrace)
                layout {
                    dragmode = DragMode.zoom
                    margin {
                        r = 10
                        t = 25
                        b = 40
                        l = 60
                    }
                    showlegend = false
                    xaxis {
                        type = AxisType.date
                        range("2017-01-03 12:00".."2017-02-15 12:00")
                        //rangeslider: { range: ["2017-01-03 12:00", "2017-02-15 12:00"] },
                        title = "Date"
                    }
                    yaxis {
                        range(114.609999778..137.410004222)
                    }
                }

                GlobalScope.launch {
                    while (isActive) {
                        delay(400)
                        candleStickTrace.open.numbers = candleStickTrace.open.doubles.map { it + Random.nextDouble() - 0.5 }
                    }
                }
            }
        }
    }
    server.show()
    println("Enter 'exit' to close server")
    while (readLine()?.trim() != "exit") {
        //wait
    }

    server.close()
}