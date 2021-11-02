package scatter

import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.GroupNorm
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.scatter


/**
 * from https://plotly.com/python/filled-area-plots/
 */
fun main() {
    val xValues = listOf("Winter", "Spring", "Summer", "Fall")

    Plotly.plot {

        fun stack(vararg ys: Number, colorOverride: String? = null) {
            scatter {
                x.strings = xValues
                y(*ys)
                hoverinfo = "x+y"
                mode = ScatterMode.lines
                line {
                    width = 0.5
                    colorOverride?.let { color(colorOverride) }
                }
                stackgroup = "one"
                groupnorm = GroupNorm.percent
            }
        }

        stack(40, 60, 40, 10, colorOverride = "rgb(131, 90, 241)")
        stack(20, 10, 10, 60, colorOverride = "rgb(111, 231, 219)")
        stack(40, 30, 50, 30, colorOverride = "rgb(184, 247, 212)")

    }.makeFile()
}