package heatmap

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import kscience.plotly.Plotly
import kscience.plotly.heatmap
import kscience.plotly.makeFile
import kscience.plotly.models.Font
import kscience.plotly.models.Text

/**
 * - Simple heatmap with annotations
 * - Change annotation font color anf size
 * - Use custom colorscale
 */
fun main() {
    val x1 = listOf("Monday", "Wednesday", "Friday", "Sunday")
    val y1 = listOf("June", "July", "August")
    val z1 = listOf(
            listOf<Number>(.1, .3, .5, .7),
            listOf<Number>(1.0, .8, .6, .4),
            listOf<Number>(.6, .4, .2, 0.0),
            listOf<Number>(.9, .7, .5, .3))
    val customColorscale = listOf(listOf(0, "navy"), listOf(1, "plum"))

    val annotationsList = mutableListOf<Text>()
    val annotationFont = Font()
    annotationFont.color("white")
    annotationFont.size = 16
    for (i in y1.indices) {
        for (j in x1.indices) {
            val curAnnotation = Text()
            curAnnotation.font = annotationFont
            curAnnotation.x = Value.of(x1[j])
            curAnnotation.y = Value.of(y1[i])
            curAnnotation.text = z1[i][j].toString()
            curAnnotation.showarrow = false
            annotationsList.add(curAnnotation)
        }
    }

    val plot = Plotly.plot {
        heatmap {
            x.set(x1)
            y.set(y1)
            z(z1)

            colorscale = Value.of(customColorscale)
            colorbar {
                tickfont { size = 16 }
            }
        }

        layout {
            xaxis {
                tickfont { size = 16 }
            }
            yaxis {
                tickfont { size = 16 }
                tickangle = -45
            }
            annotations = annotationsList
            title {
                text = "Heatmap with Custom Colorscale"
                font { size = 20 }
            }
        }
    }

    plot.makeFile()
}