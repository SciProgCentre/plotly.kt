package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.HistFunc
import scientifik.plotly.models.TraceType
import scientifik.plotly.models.XAnchor
import scientifik.plotly.trace


/**
 * - defferent binning functions
 * - default color cycle
 * - change legend border width
 */
fun main() {
    val categories = listOf("Apples","Apples","Apples","Oranges", "Bananas")
    val values = listOf("5","10","3","10","5")

    val plot = Plotly.plot2D{
        trace{
            name = "count"
            type = TraceType.histogram
            x.set(categories)
            y.set(values)
            histfunc = HistFunc.count
        }

        trace{
            name = "sum"
            type = TraceType.histogram
            x.set(categories)
            y.set(values)
            histfunc = HistFunc.sum
        }
        layout {
            width = 750
            title = "Specify Binning Function"
            legend {
                x = 1.0
                y = 1.0
                xanchor = XAnchor.auto
                borderwidth = 1
            }
        }
    }

    plot.makeFile()
}