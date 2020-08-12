package histogram

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.histogram
import kscience.plotly.makeFile
import kscience.plotly.models.HistFunc
import kscience.plotly.models.XAnchor


/**
 * - defferent binning functions
 * - default color cycle
 * - change legend border width
 */
fun main() {
    val categories = listOf("Apples", "Apples", "Apples", "Oranges", "Bananas")
    val values = listOf("5", "10", "3", "10", "5")

    val plot = Plotly.plot {
        histogram {
            name = "count"
            x.strings = categories
            y.strings = values
            histfunc = HistFunc.count
        }

        histogram {
            name = "sum"
            x.strings = categories
            y.strings = values
            histfunc = HistFunc.sum
        }

        layout {
            width = 750
            title = "Specify Binning Function"
            legend {
                x = 1
                y = 1
                xanchor = XAnchor.auto
                borderwidth = 1
            }
        }
    }

    plot.makeFile()
}