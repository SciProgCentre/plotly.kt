package io

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.scatter

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    //val file = File("D:\\Work\\Projects\\plotly.kt\\examples\\src\\main\\resources\\helloWorld.txt ")
    val resource = Plotly.javaClass.getResourceAsStream("/helloWorld.txt").readAllBytes().decodeToString()

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