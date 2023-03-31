import okio.Path.Companion.toPath
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.scatter

fun main() {
    Plotly.plot {
        scatter {
            x(1,2,3)
            y(5,8,10)
        }
    }.makeFile("plotly.html".toPath())
}