package pie

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.Pie
import kscience.plotly.models.TextInfo


/**
 * - Pie chart with many traces
 * - Use colors array as pieColors
 * - Change trace domain
 */
fun main() {
    val allLabels = listOf("1st", "2nd", "3rd", "4th", "5th")
    val allValues = listOf(
            listOf<Number>(38, 27, 18, 10, 7),
            listOf<Number>(28, 26, 21, 15, 10),
            listOf<Number>(38, 19, 16, 14, 13),
            listOf<Number>(31, 24, 19, 18, 8))
    val ultimateColors = listOf(
            listOf("rgb(56, 75, 126)", "rgb(18, 36, 37)", "rgb(34, 53, 101)", "rgb(36, 55, 57)", "rgb(6, 4, 4)"),
            listOf("rgb(177, 127, 38)", "rgb(205, 152, 36)", "rgb(99, 79, 37)", "rgb(129, 180, 179)", "rgb(124, 103, 37)"),
            listOf("rgb(33, 75, 99)", "rgb(79, 129, 102)", "rgb(151, 179, 100)", "rgb(175, 49, 35)", "rgb(36, 73, 147)"),
            listOf("rgb(146, 123, 21)", "rgb(177, 180, 34)", "rgb(206, 206, 40)", "rgb(175, 51, 21)", "rgb(35, 36, 21)"))

    val pie1 = Pie {
        values = allValues[0].map { Value.of(it) }
        labels = allLabels.map { Value.of(it) }
        name = "Starry Night"
        marker { pieColors = ultimateColors[0].map { Value.of(it) } }
        domain {
            x = listOf(0, 0.48)
            y = listOf(0, 0.48)
        }
        hoverinfo = "label+percent+name"
        textinfo = TextInfo.none
    }
    val pie2 = Pie {
        values = allValues[1].map { Value.of(it) }
        labels = allLabels.map { Value.of(it) }
        name = "Sunflowers"
        marker { pieColors = ultimateColors[1].map { Value.of(it) } }
        domain {
            x = listOf(0, 0.48)
            y = listOf(0.52, 1)
        }
        hoverinfo = "label+percent+name"
        textinfo = TextInfo.none
    }
    val pie3 = Pie {
        values = allValues[2].map { Value.of(it) }
        labels = allLabels.map { Value.of(it) }
        name = "Irises"
        marker { pieColors = ultimateColors[2].map { Value.of(it) } }
        domain {
            x = listOf(0.52, 1)
            y = listOf(0, 0.48)
        }
        hoverinfo = "label+percent+name"
        textinfo = TextInfo.none
    }
    val pie4 = Pie {
        values = allValues[3].map { Value.of(it) }
        labels = allLabels.map { Value.of(it) }
        name = "The Night Cafe"
        marker { pieColors = ultimateColors[3].map { Value.of(it) } }
        domain {
            x = listOf(0.52, 1)
            y = listOf(0.52, 1)
        }
        hoverinfo = "label+percent+name"
        textinfo = TextInfo.none
    }

    val plot = Plotly.plot {
        traces(pie1, pie2, pie3, pie4)

        layout {
            title = "Pie Chart With Many Traces"
            height = 500
            width = 500
            showlegend = false
        }
    }
    plot.makeFile()
}