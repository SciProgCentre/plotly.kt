package box

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.Box
import scientifik.plotly.models.BoxPoints

fun main() {
    val y1 = listOf<Number>(0.75, 5.25, 5.5, 6, 6.2, 6.6, 6.80, 7.0, 7.2, 7.5, 7.5, 7.75, 8.15, 8.15,
            8.65, 8.93, 9.2, 9.5, 10, 10.25, 11.5, 12, 16, 20.90, 22.3, 23.25)

    val trace1 = Box {
        y.set(y1)
        name = "All Points"
        jitter = 0.3
        pointpos = -1.8
        marker {
            color("rgb(7, 40, 89)")
        }
        boxpoints = BoxPoints.all
    }

    val trace2 = Box {
        y.set(y1)
        name = "Only Wiskers"
        marker {
            color("rgb(9,56,125)")
        }
        boxpoints = BoxPoints.`false`
    }

    val trace3 = Box {
        y.set(y1)
        name = "Suspected Outlier"
        marker {
            color("rgb(8,81,156)")
            outliercolor("rgba(219, 64, 82, 0.6)")
            line {
                outliercolor("rgba(219, 64, 82, 1.0)")
                outlierwidth = 2
            }
        }
        boxpoints = BoxPoints.suspectedoutliers
    }

    val trace4 = Box {
        y.set(y1)
        name = "Wiskers and Outliers"
        marker {
            color("rgb(107,174,214)")
        }
        boxpoints = BoxPoints.outliers
    }

    val plot = Plotly.plot {
        traces(trace1, trace2, trace3, trace4)

        layout {
            width = 700
            height = 450
            title = "Box Plot Styling Outliers"
        }
    }
    plot.makeFile()
}