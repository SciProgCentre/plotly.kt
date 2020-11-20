import hep.dataforge.meta.invoke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kscience.plotly.Plotly
import kscience.plotly.models.Bar
import kscience.plotly.plot
import kscience.plotly.server.close
import kscience.plotly.server.pushUpdates
import kscience.plotly.server.serve
import kscience.plotly.server.show
import kotlin.random.Random


fun main() {
    val initialValue = (1..10).toList()

    val traces = (0..2).associate { i ->
        val name = "Series $i"

        name to Bar {
            x.strings = initialValue.map { "Column: $it" }
            y.numbers = initialValue
            this.name = name
        }
    }

    val server = Plotly.serve(port = 3872) {

        //root level plots go to default page
        page { plotly ->
            plot(renderer = plotly) {
                traces(traces.values)
                layout {
                    title = "Other dynamic plot"
                    xaxis.title = "x axis name"
                    yaxis.title = "y axis name"
                }
            }
        }

        pushUpdates(100)       // start sending updates via websocket to the front-end
    }

    server.show()

    //Start pushing updates
    GlobalScope.launch {
        delay(1000)
        while (isActive) {
            repeat(10) { columnIndex ->
                repeat(3) { seriesIndex ->
                    delay(100)
                    traces["Series $seriesIndex"]?.let {bar->
                        println("Updating ${bar.name}, Column $columnIndex")
                        //TODO replace with dynamic data API
                        val yValues = bar.y.doubles
                        yValues[columnIndex] = Random.nextInt(0,100).toDouble()
                        bar.y.doubles = yValues
                    }
                }
            }
        }
    }

    println("Press Enter to close server")
    readLine()

    server.close()
}