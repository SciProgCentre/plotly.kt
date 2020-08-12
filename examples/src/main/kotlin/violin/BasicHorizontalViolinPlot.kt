package violin

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import io.readResourceAsCsv
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.ViolinPoints
import kscience.plotly.violin


/**
 * - Horizontal violin plot
 * - Load resource from local file using krangl
 * - Change colors of the plot
 * - Show all points
 */
fun main() {
    val df = readResourceAsCsv("/violin_data.csv")

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