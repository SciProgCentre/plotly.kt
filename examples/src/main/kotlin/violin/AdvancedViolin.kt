package violin

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.*
import scientifik.plotly.palettes.T10

fun main() {
    val trace1 = Violin {
        text = listOf("sample length: 32")
        marker {
            line {
                width = 2
                color(T10.BLUE)
            }
            symbol = Symbol.valueOf("line-ns")
        }
        orientation = Orientation.horizontal
        hoveron = ViolinHoveron.`points+kde`
        meanline {
            visible = true
        }
        legendgroup = "F"
        scalegroup = "F"
        points = ViolinPoints.all
        pointpos = 1.2
        jitter = 0.0
        box {
            visible = true
        }
        scalemode = ViolinScaleMode.count
        showlegend = false
        side = ViolinSide.positive
        y0 = Value.of(0)

        name = "F"

        x.set(listOf(10.07, 34.83, 10.65, 12.43, 24.08, 13.42, 12.48, 29.8, 14.52, 11.38,
            20.27, 11.17, 12.26, 18.26, 8.51, 10.33, 14.15, 13.16, 17.47, 27.05, 16.43,
            8.35, 18.64, 11.87, 19.81, 43.11, 13.0, 12.74, 13.0, 16.4, 16.47, 18.78))
    }

    val trace2 = Violin {
        text = listOf("sample length: 32")
        marker {
            line {
                width = 2
                color(T10.ORANGE)
            }
            symbol = Symbol.valueOf("line-ns")
        }
        orientation = Orientation.horizontal
        hoveron = ViolinHoveron.`points+kde`
        meanline {
            visible = true
        }
        legendgroup = "M"
        scalegroup = "M"
        points = ViolinPoints.all
        pointpos = -1.2
        jitter = 0.0
        box {
            visible = true
        }
        scalemode = ViolinScaleMode.count
        showlegend = false
        side = ViolinSide.negative
        y0 = Value.of(0)

        name = "M"

        x.set(listOf(27.2, 22.76, 17.29, 19.44, 16.66, 32.68, 15.98, 13.03, 18.28, 24.71,
                21.16, 11.69, 14.26, 15.95, 8.52, 22.82, 19.08, 16.0, 34.3, 41.19, 9.78,
                7.51, 28.44, 15.48, 16.58, 7.56, 10.34, 13.51, 18.71, 20.53))
    }


    val plot = Plotly.plot {
        traces(trace1, trace2)
        layout {
            title {
                text = "Advanced Violin Plot"
            }
        }
    }

    plot.makeFile()
}