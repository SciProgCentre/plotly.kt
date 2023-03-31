package misc

import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.ImageXAnchor
import space.kscience.plotly.models.ImageYAnchor
import space.kscience.plotly.scatter

fun main() {
    Plotly.plot {
        scatter {
            x(1, 2, 3)
            y(1, 2, 3)
        }
        layout {
            image {
                source = "https://images.plot.ly/language-icons/api-home/python-logo.png"
                x = 0.0
                y = 1.0
                sizex = 0.2
                sizey = 0.2
                xanchor = ImageXAnchor.right
                yanchor = ImageYAnchor.bottom
            }
            image {
                source = "https://images.plot.ly/language-icons/api-home/js-logo.png"
                xref = "x"
                yref = "y"
                x = 1.5
                y = 2.0
                sizex = 1.0
                sizey = 1.0
                xanchor = ImageXAnchor.right
                yanchor = ImageYAnchor.bottom
            }
        }
    }.makeFile()
}