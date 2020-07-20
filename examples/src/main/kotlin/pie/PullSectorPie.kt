package pie

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TextInfo
import scientifik.plotly.models.TextPosition
import scientifik.plotly.pie


/**
 * - Pie chart with pulled out sector
 * - Change start angle of pie
 * - Show labels outside the pie
 */
fun main() {
    val labels = listOf("Python", "C++", "Ruby", "Java")
    val values = listOf(215, 130, 245, 210)
    val pullSectors = listOf<Number>(0.1, 0, 0, 0)

    val plot = Plotly.plot {
        pie {
            labels(labels)
            values(values)
            pull = pullSectors
            rotation = -45
            textposition = TextPosition.outside
            textinfo = TextInfo.`label+percent`
            textfont {
                size = 16
            }
            showlegend = false
        }

        layout {
            height = 600
            width = 700
            title = "Pull Sector Pie Chart"
        }
    }
    plot.makeFile()
}