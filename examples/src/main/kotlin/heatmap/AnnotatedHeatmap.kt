package heatmap

import hep.dataforge.meta.invoke
import hep.dataforge.values.Value
import scientifik.plotly.Plotly
import scientifik.plotly.heatmap
import scientifik.plotly.makeFile
import scientifik.plotly.models.Font
import scientifik.plotly.models.Text
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * - Heatmap with annotations
 * - Change font color and size of annotations text
 * - Remove annotations arrows
 * - Change colorscale and size of plot
 */
fun main() {
    val x1 = (5..25).map { it.toDouble() / 5 }
    val y1 = (5..25).map { it.toDouble() / 5 }
    val z1 = mutableListOf<MutableList<Double>>()

    for (i in y1.indices) {
        z1.add(MutableList(x1.size){0.0})
    }

    val annotationsList = mutableListOf<Text>()

    for (i in y1.indices) {
        for (j in x1.indices) {
            z1[i][j] = sin(x1[i]).pow(10) + cos(10 + y1[j] * x1[i]) * cos(x1[i])
            val annotation = Text()
            annotation.x = Value.of(x1[j])
            annotation.y = Value.of(y1[i])
            annotation.text = z1[i][j].toString().substring(0..4)

            val annotationsFont = Font()
            annotationsFont.size = 16
            if (z1[i][j] > 0) {
                annotationsFont.color("black")
            } else {
                annotationsFont.color("white")
            }
            annotation.font = annotationsFont
            annotation.showarrow = false
            annotationsList.add(annotation)
        }
    }

    val plot = Plotly.plot {
        heatmap {
            x.set(x1)
            y.set(y1)
            z.set(z1)
            colorscale = Value.of("Viridis")
        }

        layout {
            height = 800
            width = 1400
            xaxis {
                tickcolor("white")
            }
            yaxis {
                tickcolor("white")
            }
            annotations = annotationsList
            title = "Heatmap with Annotations"
        }
    }

    plot.makeFile()
}