import hep.dataforge.meta.invoke
import hep.dataforge.meta.set
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Trace
import kscience.plotly.models.invoke

fun main() {

    val x = (0..5)
    val y = x.map { it * it }

    val trace = Trace.invoke(x, y) {
        name = "sin"
        /* The hover text definition is currently not supported.
         * We are applying it directly to configuration.
         * It is still observable in the same way as other properties but is not type safe.
         */
        set("text", x.map { "label for  $it" })
    }

    val plot = Plotly.plot {
        traces(trace)
        layout {
            title = "Plot with labels"
            xaxis { title = "x axis name" }
            yaxis { title = "y axis name" }
        }
    }

    plot.makeFile()
}
