package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*
import java.util.*

/**
 * - overlaying histograms
 * - use RGBA as color palette
 * - change bargap, bargroupgap, barmode parameters
 */
fun main() {
    val rnd = Random()
    val k = List(500) { rnd.nextDouble() }
    val x1 = k.map { it * 5 }.toList()
    val x2 = k.map { it * 10 }.toList()
    val y1 = k.map { it }.toList()
    val y2 = k.map { it * 2 }.toList()

    val trace1 = Histogram(x1, y1) {
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

    val trace2 = Histogram(x2, y2) {
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

    val plot = Plotly.plot2D {
        addTrace(trace1, trace2)
        layout {
            width = 900
            height = 500
            bargap = 0.05
            bargroupgap = 0.2
            barmode = BarMode.overlay
            title {
                text = "Sampled Results"
            }
            xaxis {
                title {
                    text = "Value"
                }
            }
            yaxis {
                title {
                    text = "Count"
                }
            }
            legend {
                x = 1.0
                y = 1.0
                xanchor = XAnchor.auto
                bgcolor("#E2E2E2")
                traceorder = TraceOrder.normal
            }
        }
    }

    plot.makeFile()
}