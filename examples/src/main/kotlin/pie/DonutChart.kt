package pie

import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.models.Pie
import scientifik.plotly.show
import scientifik.plotly.staticPlot


fun main() {
    val donut1 = Pie {
        values(listOf<Number>(16, 15, 12, 6, 5, 4, 42))
        labels(listOf("US", "China", "European Union", "Russian Federation", "Brazil", "India", "Rest of World"))
        hole = 0.4
    }

    val donut2 = Pie {
        values(listOf<Number>(27, 11, 25, 8, 1, 3, 25))
        labels(listOf("US", "China", "European Union", "Russian Federation", "Brazil", "India", "Rest of World"))
        hole = 0.4
    }

    Plotly.show {
        staticPlot {
            traces(donut1)

            layout {
                width = 600
                height = 600
                title = "GHG Emissions"
            }
        }

        staticPlot {
            traces(donut2)

            layout {
                width = 600
                height = 600
                title = "CO2"
            }
        }
    }
}