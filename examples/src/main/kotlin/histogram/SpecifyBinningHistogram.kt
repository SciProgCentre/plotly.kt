package histogram

import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.HisFunc
import scientifik.plotly.models.Type
import scientifik.plotly.trace
import java.util.*


fun main() {
    val x = listOf("Apples","Apples","Apples","Organges", "Bananas")
    val y = listOf("5","10","3","10","5")

    val plot = Plotly.plot2D{
        trace{
            name = "count"
            type = Type.histogram
            this.x = x
            this.y = y
            histfunc = HisFunc.count
        }
        trace{
            name = "sum"
            type = Type.histogram
            this.x = x
            this.y = y
            histfunc = HisFunc.sum
        }
        layout {
            title = "Specify Binning Function"
        }
    }

    plot.makeFile()
}