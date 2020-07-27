package io

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.scatter


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