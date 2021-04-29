package plots3d

import space.kscience.dataforge.meta.set
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.trace

fun main() {
    val plot = Plotly.plot {
        trace {
            set("type","scatter3d")
            x(1,2,3)
            y(1,2,3)
            z(1,2,3)
        }
    }
    plot.makeFile()
}