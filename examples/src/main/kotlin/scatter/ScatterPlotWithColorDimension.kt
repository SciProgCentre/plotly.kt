package scatter

import space.kscience.dataforge.meta.invoke
import space.kscience.dataforge.values.Value
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Scatter
import space.kscience.plotly.models.ScatterMode


/**
 * - Scatter plot only with markers
 * - Use numbers as color list
 */
fun main() {
    val scatter = Scatter {
        y.set(List(40) { 5.0 })
        mode = ScatterMode.markers
        marker {
            size = 40
            colors(List(40) { Value.of(it) })
        }
    }

    val plot = Plotly.plot {
        traces(scatter)

        layout {
            title = "Scatter plot with color dimension"
        }
    }
    plot.makeFile()
}
