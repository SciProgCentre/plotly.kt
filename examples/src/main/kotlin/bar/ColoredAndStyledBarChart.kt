package bar

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Bar
import kscience.plotly.models.BarMode


/**
 * - Colored and styled bar chart
 * - Change font size and color of axis title & labels
 * - Use grouped barmode
 */
fun main() {
    val years = listOf(1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012)
    val trace1 = Bar {
        x.set(years)
        y(219, 146, 112, 127, 124, 180, 236, 207, 236, 263, 350, 430, 474, 526, 488, 537, 500, 439)
        name = "Rest of world"
        marker {
            color("rgb(55, 83, 109)")
        }
    }

    val trace2 = Bar {
        x.set(years)
        y(16, 13, 10, 11, 28, 37, 43, 55, 56, 88, 105, 156, 270, 299, 340, 403, 549, 499)
        name = "China"
        marker {
            color("rgb(26, 118, 255)")
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "US Export of Plastic Scrap"
            xaxis {
                tickfont {
                    size = 14
                    color("rgb(107, 107, 107)")
                }
            }
            yaxis {
                title {
                    text = "USD (millions)"
                    font {
                        size = 16
                        color("rgb(107, 107, 107)")
                    }
                }
                tickfont {
                    size = 14
                    color("rgb(107, 107, 107)")
                }
            }
            legend {
                x = 0
                y = 1.0
                bgcolor("rgba(255, 255, 255, 0)")
                bordercolor("rgba(255, 255, 255, 0)")
            }

            barmode = BarMode.group
            bargap = 0.15
            bargroupgap = 0.1
        }
    }
    plot.makeFile()
}