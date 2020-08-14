package pie

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Pie
import kscience.plotly.palettes.Xkcd


/**
 * - Nested pie chart
 * - Set sectors colors using marker -> colors
 * - Change legend borderwidth
 */
fun main() {
    val labels = listOf("party A", "party B", "party C", "party D")
    val colorsPie = listOf(Xkcd.RED, Xkcd.GREEN, Xkcd.ORANGE, Xkcd.BLUE).map { Value.of(it) }

    val firstPie = Pie {
        name = "First day"
        values(listOf(3, 2, 2, 3))
        labels(labels)
        hole = 0.5
        sort = false
        textfont { size = 16 }
        domain {
            x = listOf<Number>(0.2, 0.8)
            y = listOf<Number>(0.1, 0.9)
        }
        marker {
            pieColors = colorsPie
            line {
                color("white")
                width = 2
            }
        }
    }

    val secondPie = Pie {
        name = "Second day"
        values(listOf(8, 7, 6, 5))
        labels(labels)
        hole = 0.77
        opacity = 0.75
        sort = false
        textfont { size = 16 }
        domain {
            x = listOf<Number>(0.1, 0.9)
            y = listOf<Number>(0, 1)
        }
        marker {
            pieColors = colorsPie
            line {
                color("white")
                width = 2
            }
        }
    }

    val plot = Plotly.plot {
        traces(firstPie, secondPie)

        layout {
            width = 700
            height = 600
            title = "Nested Pie Charts"

            legend {
                borderwidth = 1
                font {
                    size = 16
                }
            }
        }
    }
    plot.makeFile()
}