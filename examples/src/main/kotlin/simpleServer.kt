import hep.dataforge.meta.invoke
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import scientifik.plotly.Plot2D
import scientifik.plotly.Plotly
import scientifik.plotly.models.Trace
import scientifik.plotly.plot
import scientifik.plotly.server.serve
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
fun main() {
    val server = Plotly.serve {
        val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()

        val trace1 = Trace(x, y1) { name = "sin" }
        val trace2 = Trace(x, y2) { name = "cos" }

        lateinit var plot1: Plot2D

        //root level plots go to default page
        page {
            table {
                tr {
                    td {
                        style = "width = 66%;"
                        plot1 = plot {
                            traces(trace1, trace2)
                            layout {
                                title = "First graph, row: 1, size: 8/12"
                                xaxis { title = "x axis name" }
                                yaxis { title = "y axis name" }
                            }
                        }
                    }
                    td {
                        style = "width = 33%;"
                        plot {
                            traces(trace1, trace2)
                            layout {
                                title = "Second graph, row: 1, size: 4/12"
                                xaxis { title = "x axis name" }
                                yaxis { title = "y axis name" }
                            }
                        }
                    }
                }
            }

            plot {
                traces(trace1, trace2)
                layout {
                    title = "Third graph, row: 2, size: 12/12"
                    xaxis { title = "x axis name" }
                    yaxis { title = "y axis name" }
                }
            }
        }

        page("other") {
            plot(plot1)
        }

    }

    server.show()

    println("Press Enter to close server")
    readLine()

    server.close()

}