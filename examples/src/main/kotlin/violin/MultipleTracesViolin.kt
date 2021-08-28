package violin

import org.jetbrains.dataframe.DataFrame
import org.jetbrains.dataframe.column
import org.jetbrains.dataframe.filter
import org.jetbrains.dataframe.io.readCSV
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

    val day by column<String>()

    val thursdayDf = df.filter { day() == "Thur" }
    val fridayDf = df.filter { day() == "Fri" }
    val saturdayDf = df.filter { day() == "Sat" }
    val sundayDf = df.filter { day() == "Sun" }

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