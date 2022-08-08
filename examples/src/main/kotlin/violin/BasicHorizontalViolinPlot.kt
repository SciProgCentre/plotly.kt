package violin

import io.readResourceAsCsv
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ViolinPoints
import space.kscience.plotly.violin


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