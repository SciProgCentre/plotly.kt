package violin

import io.fromDataFrame
import io.readResourceAsCsv
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.filter
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Violin
import space.kscience.plotly.models.ViolinMode


/**
 * - Grouped violin plot
 * - Load csv from local file using krangl
 */
fun main() {
    val df = readResourceAsCsv("/violin_data.csv")

    val sex by column<String>()

    val violin1 = Violin {
        val males = df.filter { sex() == "Male" }

        x.fromDataFrame(males, "day")
        y.fromDataFrame(males, "total_bill")
        legendgroup = "M"
        scalegroup = "M"
        name = "M"
        box { visible = true }
        line { color("blue") }
        meanline { visible = true }
    }

    val violin2 = Violin {
        val females = df.filter { sex() == "Female" }

        x.fromDataFrame(females, "day")
        y.fromDataFrame(females, "total_bill")
        legendgroup = "F"
        scalegroup = "F"
        name = "F"
        box { visible = true }
        line { color("pink") }
        meanline { visible = true }
    }

    val plot = Plotly.plot {
        traces(violin1, violin2)

        layout {
            title = "Grouped Violin Plot"
            yaxis { zeroline = false }
            violinmode = ViolinMode.group
        }
    }
    plot.makeFile()
}