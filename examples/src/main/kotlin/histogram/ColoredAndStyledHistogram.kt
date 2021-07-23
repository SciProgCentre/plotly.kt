package histogram

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.*
import java.util.*

/**
 * - overlaying histograms
 * - use RGBA as color palette
 * - change bargap, bargroupgap, barmode parameters
 */
fun main() {
    val rnd = Random()
    val k = List(500) { rnd.nextDouble() }

    val trace1 = Histogram {
        x.numbers = k.map { it * 5 }
        y.numbers = k.map { it }
        name = "control"
        histfunc = HistFunc.count
        marker {
            color(255, 50, 102, 0.7)
        }
        opacity = 0.5
        xbins {
            end = 2.8
            start = 0.5
            size = 0.06
        }
    }

    val trace2 = Histogram {
        x.numbers = k.map { it * 10 }
        y.numbers = k.map { it*2 }
        name = "experimental"
        marker {
            color(0, 100, 255, 0.7)
        }
        opacity = 0.75
        xbins {
            end = 4.0
            start = -3.2
            size = 0.06
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)
        layout {
            width = 900
            height = 500
            bargap = 0.05
            bargroupgap = 0.2
            barmode = BarMode.overlay
            title = "Sampled Results"
            xaxis {
                title = "Value"
            }
            yaxis {
                title = "Count"
            }
            legend {
                x = 1
                y = 1
                xanchor = XAnchor.auto
                bgcolor("#E2E2E2")
                traceorder = TraceOrder.normal
            }
        }
    }

    plot.makeFile()
}