package misc

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import space.kscience.plotly.Plotly
import space.kscience.plotly.histogram
import space.kscience.plotly.layout
import space.kscience.plotly.server.close
import space.kscience.plotly.server.plot
import space.kscience.plotly.server.serve
import kotlin.random.Random

fun main() {
    val server = Plotly.serve {
        val rnd = Random(222)
        plot {
            histogram {
                name = "Random data"
                GlobalScope.launch {
                    while (isActive) {
                        x.numbers = List(500) { rnd.nextDouble() }
                        delay(300)
                    }
                }
            }

            layout {
                bargap = 0.1
                title {
                    text = "Basic Histogram"
                    font {
                        size = 20
                        color("black")
                    }
                }
                xaxis {
                    title {
                        text = "Value"
                        font {
                            size = 16
                        }
                    }
                }
                yaxis {
                    title {
                        text = "Count"
                        font {
                            size = 16
                        }
                    }
                }
            }
        }
    }

    println("Press Enter to close server")
    readLine()

    server.close()

}