package bar

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.bar
import scientifik.plotly.makeFile


/**
 * - Colored bar chart
 * - Use array of colors
 * - Make bar chart without making Bar piece
 * - Customizing individual bar colors and width
 */
fun main() {
    val plot = Plotly.plot {
        bar {
            x("Feature A", "Feature B", "Feature C", "Feature D", "Feature E")
            y(20, 14, 23, 25, 22)
            width = listOf<Number>(0.8, 0.8, 0.9, 0.6, 0.8)
            marker {
                colors(listOf("rgba(204,204,204,1)",
                        "rgba(222,45,38,0.8)",
                        "rgba(204,204,204,1)",
                        "rgba(204,204,204,1)", "rgba(204,204,204,1)"))
            }
        }

        layout {
            title = "Least Used Feature"
        }
    }
    plot.makeFile()
}