package scientifik.plotly

import tornadofx.*

class PlotlyController : Controller() {

    val server = MyPlotServer()

    fun changeScale(value: Number) {
        server.scale.set(value.toLong())
    }

    data class PlotItem(val name: String) {
        val address: String
            get() = "http://localhost:7777/pages/${name}"
    }

    val plots = listOf(
        PlotItem("Dynamic"),
        PlotItem("Static")
    ).asObservable()
}