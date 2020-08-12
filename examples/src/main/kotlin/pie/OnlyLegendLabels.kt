package pie

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.TextInfo
import kscience.plotly.pie

fun main() {
    val labels = listOf("Cookies", "Jellybean", "Milkshake", "Cheesecake")
    val values = listOf(38.4, 40.6, 20.7, 10.3)

    val plot = Plotly.plot {
        pie {
            labels(labels)
            values(values)
            textinfo = TextInfo.none
        }

        layout {
            width = 700
            height = 600
            title = "Show Labels Only in Legend"
            legend {
                borderwidth = 1
                font {
                    size = 16
                }
            }
        }
    }
    plot.makeFile()
}