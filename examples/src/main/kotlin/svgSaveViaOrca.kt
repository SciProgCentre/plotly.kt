import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.export
import kscience.plotly.models.Scatter
import kscience.plotly.models.ScatterMode
import kscience.plotly.selectFile
import javax.swing.filechooser.FileNameExtensionFilter


/**
 * - Scatter plot with many traces
 * - Hide axis & grid lines
 * - Export picture as svg using orca
 */
fun main() {
    val trace1 = Scatter {
        x(52698, 43117)
        y(53, 31)
        mode = ScatterMode.markers
        name = "North America"
        textsList = listOf("United States", "Canada")
        marker {
            color("rgb(164, 194, 244)")
            size = 12
            line {
                color("white")
                width = 1
            }
        }
    }

    val trace2 = Scatter {
        x(39317, 37236, 35650, 30066, 29570, 27159, 23557, 21046, 18007)
        y(33, 20, 13, 19, 27, 19, 49, 44, 38)
        mode = ScatterMode.markers
        name = "Europe"
        textsList = listOf("Germany", "Britain", "France", "Spain", "Italy", "Czech Rep.", "Greece", "Poland")
        marker {
            color("rgb(255, 217, 102)")
            size = 12
        }
    }

    val trace3 = Scatter {
        x(42952, 37037, 33106, 17478, 9813, 5253, 4692, 3899)
        y(23, 42, 54, 89, 14, 99, 93, 70)
        mode = ScatterMode.markers
        name = "Asia/Pacific"
        textsList =
            listOf("Australia", "Japan", "South Korea", "Malaysia", "China", "Indonesia", "Philippines", "India")
        marker {
            color("rgb(234, 153, 153)")
            size = 12
        }
    }

    val trace4 = Scatter {
        x(19097, 18601, 15595, 13546, 12026, 7434, 5419)
        y(43, 47, 56, 80, 86, 93, 80)
        mode = ScatterMode.markers
        name = "Latin America"
        textsList = listOf("Chile", "Argentina", "Mexico", "Venezuela", "Venezuela", "El Salvador", "Bolivia")
        marker {
            color("rgb(142, 124, 195)")
            size = 12
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2, trace3, trace4)

        layout {
            title = "Quarter 1 Growth"
            xaxis {
                title = "GDP per Capita"
                showgrid = false
                zeroline = false
            }
            yaxis {
                title = "Percent"
                showline = false
            }
        }
    }

    plot.export(selectFile(FileNameExtensionFilter("SVG","svg")) ?: error("File not selected"))
}