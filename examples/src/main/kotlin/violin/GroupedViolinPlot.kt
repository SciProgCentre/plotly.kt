package violin

import hep.dataforge.meta.invoke
import krangl.DataFrame
import krangl.eq
import krangl.readCSV
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Violin
import scientifik.plotly.models.ViolinMode


/**
 * - Grouped violin plot
 * - Load resources from local file using krangl
 */
fun main() {
    val path = "examples/src/main/kotlin/violin/violin_data.csv"
    val df = DataFrame.readCSV(path)

    val violin1 = Violin {
        x.set(df.filter { it["sex"] eq "Female" }["day"].values())
        y.set(df.filter { it["sex"] eq "Female" }["total_bill"].values())
        legendgroup = "M"
        scalegroup = "M"
        name = "M"
        box { visible = true }
        line { color("blue") }
        meanline { visible = true }
    }

    val violin2 = Violin {
        x.set(df.filter { it["sex"] eq "Male" }["day"].values())
        y.set(df.filter { it["sex"] eq "Male" }["total_bill"].values())
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