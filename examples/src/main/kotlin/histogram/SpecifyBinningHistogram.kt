package histogram

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.HisFunc
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace


fun main() {
    val categories = listOf("Apples","Apples","Apples","Organges", "Bananas")
    val values = listOf("5","10","3","10","5")

    val plot = Plotly.plot2D{
        trace{
            name = "count"
            type = TraceType.histogram
            x.set(categories)
            y.set(values)
            histfunc = HisFunc.count
        }

        trace{
            name = "sum"
            type = TraceType.histogram
            x.set(categories)
            y.set(values)
            histfunc = HisFunc.sum
        }
        layout {
            title = "Specify Binning Function"
        }
    }

    plot.makeFile()
}