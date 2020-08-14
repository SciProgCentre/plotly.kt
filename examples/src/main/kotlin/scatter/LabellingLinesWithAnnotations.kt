package scatter

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.*


/**
 * - Styled scatter plot
 * - Use array of traces
 * - Style annotations
 */
fun main() {
    val xData = listOf(2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2013)
    val yData = listOf(
            listOf(74, 82, 80, 74, 73, 72, 74, 70, 70, 66, 66, 69),
            listOf(45, 42, 50, 46, 36, 36, 34, 35, 32, 31, 31, 28),
            listOf(13, 14, 20, 24, 20, 24, 24, 40, 35, 41, 43, 50),
            listOf(18, 21, 18, 21, 16, 14, 13, 18, 17, 16, 19, 23))
    val colors = listOf("rgba(67, 67, 67, 1)", "rgba(115, 115, 115, 1)",
            "rgba(49, 130, 189, 1)", "rgba(189, 189, 189, 1)")
    val lineSize = listOf(2, 2, 4, 2)
    val labels = listOf("Television", "Newspaper", "Internet", "Radio")

    val tracesList = mutableListOf<Trace>()
    for (i in yData.indices) {
        val result = Scatter {
            x.set(xData)
            y.set(yData[i])
            line {
                color(colors[i])
                width = lineSize[i]
            }
        }
        tracesList.add(result)

        val result2 = Scatter {
            x.set(listOf(xData[0], xData[11]))
            y.set(listOf(yData[i][0], yData[i][11]))
            mode = ScatterMode.markers
            marker {
                color(colors[i])
                size = 12
            }
        }
        tracesList.add(result2)
    }

    val annotationsList = mutableListOf<Text>()
    val text1 = Text {
        xref = "paper"
        yref = "paper"
        x = Value.of(0.0)
        y = Value.of(1.05)
        xanchor = XAnchor.left
        yanchor = YAnchor.bottom
        text = "Main Source for News"
        font {
            family = "Arial"
            size = 30
            color("rgb(37, 37, 37)")
        }
        showarrow = false
    }
    annotationsList.add(text1)
    val text2 = Text {
        xref = "paper"
        yref = "paper"
        x = Value.of(0.5)
        y = Value.of(-0.1)
        xanchor = XAnchor.center
        yanchor = YAnchor.top
        text = "Source: Pew Research Center & Storytelling with data"
        showarrow = false
        font {
            family = "Arial"
            size = 12
            color("rgb(150, 150, 150)")
        }
    }
    annotationsList.add(text2)
    for (i in yData.indices) {
        val result = Text {
            xref = "paper"
            x = Value.of(0.05)
            y = Value.of(yData[i][0])
            xanchor = XAnchor.right
            yanchor = YAnchor.middle
            text = labels[i] + " " + yData[i][0] + "%"
            showarrow = false
            font {
                family = "Arial"
                size = 16
                color("black")
            }
        }
        annotationsList.add(result)

        val result2 = Text {
            xref = "paper"
            x = Value.of(0.95)
            y = Value.of(yData[i][11])
            xanchor = XAnchor.left
            yanchor = YAnchor.middle
            text = yData[i][11].toString() + "%"
            font {
                family = "Arial"
                size = 16
                color("black")
            }
            showarrow = false
        }
        annotationsList.add(result2)
    }

    val plot = Plotly.plot {
        traces(tracesList)

        layout {
            showlegend = false
            height = 600
            width = 600
            xaxis {
                showline = true
                showgrid = false
                showticklabels = true
                linecolor("rgb(204, 204, 204)")
                linewidth = 2
                autotick = false
                ticks = Ticks.outside
                tickcolor("rgb(204, 204, 204)")
                tickwidth = 2
                ticklen = 5
                tickfont {
                    family = "Arial"
                    size = 12
                    color("rgb(82, 82, 82)")
                }
            }

            yaxis {
                showgrid = false
                zeroline = false
                showline = false
                showticklabels = false
            }

            autosize = false
            margin {
                autoexpand = false
                l = 100
                r = 20
                t = 100
            }

            annotations = annotationsList
        }
    }
    plot.makeFile()
}