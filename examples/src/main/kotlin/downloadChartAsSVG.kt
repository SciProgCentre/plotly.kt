import kscience.plotly.*
import kscience.plotly.models.ScatterMode


/**
 * - Bubble chart with different marker sizes
 * - Download plot as SVG using configuration button
 */
fun main() {
    val fragment = Plotly.fragment {
        val plotConfig = PlotlyConfig{
            saveAsSvg()
        }

        plot(config = plotConfig) {
            scatter {
                x(1, 2, 3, 4)
                y(10, 11, 12, 13)
                textsList = listOf("A<br>size: 40", "B<br>size: 60", "C<br>size: 80", "D<br>size: 100")
                mode = ScatterMode.markers
                marker {
                    colors(listOf("rgb(93, 164, 214)", "rgb(255, 144, 14)", "rgb(44, 160, 101)", "rgb(255, 65, 54)"))
                    sizesList = listOf(40, 60, 80, 100)
                }
            }

            layout {
                title = "Download Chart as SVG instead of PNG"
                showlegend = false
                height = 600
                width = 600
            }
        }
    }
    fragment.makeFile()
}