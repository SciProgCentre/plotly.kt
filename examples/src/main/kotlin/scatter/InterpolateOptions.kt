package scatter

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.LineShape
import scientifik.plotly.models.Scatter
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.models.TraceOrder


/**
 * - Interpolate options
 * - Legend styling
 * - Choose scatter mode and line shapes
 */
fun main() {
    val trace1 = Scatter {
        x(1, 2, 3, 4, 5)
        y(1, 3, 2, 3, 1)
        mode = ScatterMode.`lines+markers`
        name = "linear"
        line {
            shape = LineShape.linear
        }
    }

    val trace2 = Scatter {
        x(1, 2, 3, 4, 5)
        y(6, 8, 7, 8, 6)
        mode = ScatterMode.`lines+markers`
        name = "spline"
        text = listOf("tweak line smoothness<br>with \"smoothing\" in line object",
                "tweak line smoothness<br>with \"smoothing\" in line object",
                "tweak line smoothness<br>with \"smoothing\" in line object",
                "tweak line smoothness<br>with \"smoothing\" in line object",
                "tweak line smoothness<br>with \"smoothing\" in line object",
                "tweak line smoothness<br>with \"smoothing\" in line object")
        line {
            shape = LineShape.spline
        }
    }

    val trace3 = Scatter {
        x(1, 2, 3, 4, 5)
        y(11, 13, 12, 13, 11)
        mode = ScatterMode.`lines+markers`
        name = "vhv"
        line {
            shape = LineShape.vhv
        }
    }

    val trace4 = Scatter {
        x(1, 2, 3, 4, 5)
        y(16, 18, 17, 18, 16)
        mode = ScatterMode.`lines+markers`
        name = "hvh"
        line {
            shape = LineShape.hvh
        }
    }

    val trace5 = Scatter {
        x(1, 2, 3, 4, 5)
        y(21, 23, 22, 23, 21)
        mode = ScatterMode.`lines+markers`
        name = "vh"
        line {
            shape = LineShape.vh
        }
    }

    val trace6 = Scatter {
        x(1, 2, 3, 4, 5)
        y(26, 28, 27, 28, 26)
        mode = ScatterMode.`lines+markers`
        name = "hv"
        line {
            shape = LineShape.hv
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2, trace3, trace4, trace5, trace6)

        layout {
            legend {
                y = 0.5
                traceorder = TraceOrder.reversed
                font { size = 16 }
            }
        }
    }
    plot.makeFile()
}