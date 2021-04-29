package io

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.scatter


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