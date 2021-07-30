package errorPlots

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.FillType
import space.kscience.plotly.models.Scatter
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.models.Ticks


/**
 * - Scatter plot with filled error lines
 * - Use rgb(a) color palette
 * - Change colors of grid, paper background and line fills
 */
fun main() {
    val trace1 = Scatter {
        x(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        y(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
        fill = FillType.tozerox
        fillcolor("rgba(0, 100, 80, 0.2)")
        line {
            color("transparent")
        }
        name = "Fair"
        showlegend = false
    }

    val trace2 = Scatter {
        x(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        y(5.5, 3, 5.5, 8, 6, 3, 8, 5, 6, 5.5, 4.75, 5, 4, 7, 2, 4, 7, 4.4, 2, 4.5)
        fill = FillType.tozerox
        fillcolor("rgba(0, 176, 246, 0.2)")
        line {
            color("transparent")
        }
        name = "Premium"
        showlegend = false
    }

    val trace3 = Scatter {
        x(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        y(11, 9, 7, 5, 3, 1, 3, 5, 3, 1, -1, 1, 3, 1, -0.5, 1, 3, 5, 7, 9)
        fill = FillType.tozerox
        fillcolor("rgba(231, 107, 243, 0.2)")
        line {
            color("transparent")
        }
        name = "Fair"
        showlegend = false
    }

    val trace4 = Scatter {
        x(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        y(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        line {
            color("rgb(0, 100, 80)")
        }
        mode = ScatterMode.lines
        name = "Fair"
    }

    val trace5 = Scatter {
        x(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        y(5, 2.5, 5, 7.5, 5, 2.5, 7.5, 4.5, 5.5, 5)
        line {
            color("rgb(0, 176, 246)")
        }
        mode = ScatterMode.lines
        name = "Premium"
    }

    val trace6 = Scatter {
        x(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        y(10, 8, 6, 4, 2, 0, 2, 4, 2, 0)
        line {
            color("rgb(231, 107, 243)")
        }
        mode = ScatterMode.lines
        name = "Ideal"
    }

    val plot = Plotly.plot {
        traces(trace1, trace2, trace3, trace4, trace5, trace6)

        layout {
            paper_bgcolor("rgb(255, 255, 255)")
            plot_bgcolor("rgb(229, 229, 229)")
            xaxis {
                gridcolor("rgb(255, 255, 255)")
                range(1.0..10.0)
                showgrid = true
                showline = false
                showticklabels = true
                tickcolor("rgb(127, 127, 127)")
                ticks = Ticks.outside
                zeroline = false
            }
            yaxis {
                gridcolor("rgb(255, 255, 255)")
                showgrid = true
                showline = false
                showticklabels = true
                tickcolor("rgb(127, 127, 127)")
                ticks = Ticks.outside
                zeroline = false
            }
        }
    }
    plot.makeFile()
}