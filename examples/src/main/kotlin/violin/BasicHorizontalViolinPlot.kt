package violin

import hep.dataforge.values.Value
import hep.dataforge.meta.*
import krangl.DataFrame
import krangl.readCSV
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ViolinPoints
import scientifik.plotly.violin


/**
 * - Horizontal violin plot
 * - Load resource from local file using krangl
 * - Change colors of the plot
 * - Show all points
 */
fun main() {
    val path = "examples/src/main/kotlin/violin/violin_data.csv"
    val df = DataFrame.readCSV(path)

    val plot = Plotly.plot {
        violin {
            x.set(df["total_bill"].values())
            points = ViolinPoints.all
            box { visible = true }
            line { color("black") }
            fillcolor("#8dd3c7")
            opacity = 0.6
            meanline { visible = true }
            y0 = Value.of("Total Bill")
        }

        layout {
            title = "Basic Horizontal Violin Plot"
            xaxis { zeroline = false }
        }
    }
    plot.makeFile()
}