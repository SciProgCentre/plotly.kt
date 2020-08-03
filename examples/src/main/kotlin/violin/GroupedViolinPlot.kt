package violin

import hep.dataforge.meta.invoke
import io.readResourceAsCsv
import krangl.DataCol
import krangl.DataFrame
import krangl.eq
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.TraceValues
import scientifik.plotly.models.Violin
import scientifik.plotly.models.ViolinMode


/**
 * Extension function for using krangl data columns as axis values
 */
fun TraceValues.column(column: DataCol) {
    set(column.values())
}

fun TraceValues.column(frame: DataFrame, column: String) {
    column(frame[column])
}


/**
 * - Grouped violin plot
 * - Load csv from local file using krangl
 */
fun main() {
    val df = readResourceAsCsv("/violin_data.csv")

    val violin1 = Violin {
        val males = df.filter { it["sex"] eq "Male" }

        x.column(males, "day")
        y.column(males, "total_bill")
        legendgroup = "M"
        scalegroup = "M"
        name = "M"
        box { visible = true }
        line { color("blue") }
        meanline { visible = true }
    }

    val violin2 = Violin {
        val females = df.filter { it["sex"] eq "Female" }

        x.column(females, "day")
        y.column(females, "total_bill")
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