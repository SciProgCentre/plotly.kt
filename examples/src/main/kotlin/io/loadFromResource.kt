package io

import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.ScatterMode
import kscience.plotly.scatter


fun main() {
    val resource = readResourceAsString("/simpleData.txt")

    val data = resource.lines().map { it.split(" ").map { it.toInt() } }

    val plot = Plotly.plot {
        scatter {
            x.numbers = data[0]
            y.numbers = data[1]
            mode = ScatterMode.markers
        }

        layout {
            title {
                text = "Load from resource"
            }
        }
    }

    plot.makeFile()
}