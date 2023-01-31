package plots3d

import space.kscience.plotly.Plotly
import space.kscience.plotly.UnsupportedPlotlyAPI
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.TraceType
import space.kscience.plotly.trace

@OptIn(UnsupportedPlotlyAPI::class)
fun main() {
    val plot = Plotly.plot {
        trace {
            type = TraceType.scatter3d
            x(1,2,3)
            y(1,2,3)
            z(1,2,3)
        }
    }
    plot.makeFile()
}