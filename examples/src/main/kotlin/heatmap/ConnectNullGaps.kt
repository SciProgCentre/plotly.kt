package heatmap

import kscience.plotly.Plotly
import kscience.plotly.fragment
import kscience.plotly.layout
import kscience.plotly.models.Heatmap
import kscience.plotly.plot


/**
 * - heatmaps with and without gaps between not-null values
 * - change plot size
 * - stack two plots
 */
fun main() {
    val x1 = (0..7)
    val y2 = (0..6)

    val z1 = listOf(
            listOf<Number?>(null, null, null, 12, 13, 14, 15, 16),
            listOf<Number?>(null, 1, null, 11, null, null, null, 17),
            listOf<Number?>(null, 2, 6, 7, null, null, null, 18),
            listOf<Number?>(null, 3, null, 8, null, null, null, 19),
            listOf<Number?>(5, 4, 10, 9, null, null, null, 20),
            listOf<Number?>(null, null, null, 27, null, null, null, 21),
            listOf<Number?>(null, null, null, 26, 25, 24, 23, 22))

    val heatmap1 = Heatmap {
        x.set(x1)
        y.set(y2)
        z.set(z1)
        showscale = false
    }

    val heatmap2 = Heatmap {
        x.set(x1)
        y.set(y2)
        z.set(z1)
        showscale = false
        connectgaps = true
    }

    Plotly.fragment {
        plot {
            traces(heatmap1)
            layout {
                width = 800
                height = 450
                title = "Gaps Between Nulls"
            }
        }

        plot {
            traces(heatmap2)
            layout {
                width = 800
                height = 450
                title = "Connected Gaps"
            }
        }
    }
}
