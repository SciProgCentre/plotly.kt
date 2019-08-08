package scientifik.plotly

import tornadofx.*

class MyApp : App(PlotlyView::class)

fun main(args: Array<String>) {
    launch<MyApp>(args)
}
