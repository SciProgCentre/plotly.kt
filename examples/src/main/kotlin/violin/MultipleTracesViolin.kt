package violin

import krangl.DataFrame
import krangl.eq
import krangl.readCSV
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.violin


/**
 * - Violin plot with multiple traces
 * - Load resources from url using krangl
 */
fun main() {
    val csvUrl = "https://raw.githubusercontent.com/plotly/datasets/master/violin_data.csv"
    val df = DataFrame.readCSV(csvUrl)

    val thursdayDf = df.filter { it["day"] eq "Thur" }
    val fridayDf = df.filter { it["day"] eq "Fri" }
    val saturdayDf = df.filter { it["day"] eq "Sat" }
    val sundayDf = df.filter { it["day"] eq "Sun" }

    val plot = Plotly.plot {
        violin {
            name = "Thursday"
            y.set(thursdayDf["total_bill"].values())

            line { color("green") }
            box { visible = true }
            meanline { visible = true }
        }

        violin {
            name = "Friday"
            y.set(fridayDf["total_bill"].values())

            line { color("red") }
            box { visible = true }
            meanline { visible = true }
        }

        violin {
            name = "Saturday"
            y.set(saturdayDf["total_bill"].values())

            line { color("orange") }
            box { visible = true }
            meanline { visible = true }
        }

        violin {
            name = "Sunday"
            y.set(sundayDf["total_bill"].values())

            line { color("blue") }
            box { visible = true }
            meanline { visible = true }
        }

        layout {
            width = 700
            height = 450
            title = "Multiple Traces Violin Plot"

            yaxis {
                zeroline = false
            }
        }
    }
    plot.makeFile()
}