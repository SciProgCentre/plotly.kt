package tutorials

import space.kscience.dataforge.meta.Value
import space.kscience.plotly.*
import space.kscience.plotly.models.*
import space.kscience.plotly.palettes.Xkcd
import kotlin.math.PI
import kotlin.math.sin


/**
 * - Draw Math Graph with LaTeX annotations
 * - Custom zeroline
 * - Dashed and colored lines
 * - Draw vertical lines
 * - Use XKCD color palette
 * - Change margins on the plot edges
 * - Add shapes (vertical lines)
 */
fun main() {
    val div = 200 / PI
    val sub = PI / 6
    val xValues: List<Double> = (-410..410).map { it / div }
    val yValues = mutableListOf<Double>()
    val yText = mutableListOf<String>()

    for (elem in xValues) {
        val x = sin(elem)
        yValues.add(x)
        yText.add("sin = $x")
    }

    val xElems = listOf(-2 * PI + sub, -PI - sub, sub, PI - sub)
    val shapesList = xElems.map { elem ->
        Shape {
            xref = "x"
            yref = "y"
            startXY(elem, 0)
            endXY(elem, 0.5)
            line { color("red") }
        }
    }

    Plotly.page(mathJaxHeader, cdnPlotlyHeader) {
        plot {
            scatter { // sinus
                x.set(xValues)
                y.set(yValues)
                name = "\$\\Large{y = \\mathrm{sin}\\,x}\$"
                text.strings = yText
                hoverinfo = "text"
                line { color(Xkcd.CERULEAN) }
            }
            scatter { // sin(x) = 0
                mode = ScatterMode.markers
                x(-2 * PI, -PI, PI, 2 * PI)
                y(0, 0, 0, 0)
                line { color(Xkcd.CERULEAN_BLUE) }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }
            scatter { // sin(x) = 1/2
                mode = ScatterMode.markers
                x.set(xElems + xElems)
                y(0.5, 0.5, 0.5, 0.5, 0, 0, 0, 0)
                line { color("red") }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                mode = ScatterMode.text
                x(-0.35, 0.28, PI * 2 + 0.4)
                y(0.56, 1.3, 0.1)
                text.strings = listOf("\$\\Large{1/2}\$", "\$\\huge{y}\$", "\$\\huge{x}\$")
                textfont {
                    colors(listOf("red", "black", "black"))
                }
                showlegend = false
                hoverinfo = "none"
            }

            layout {
                height = 500
                width = 900
                shapes = shapesList
                margin { l = 20; r = 20; b = 20; t = 50 }

                text { // arrow OX
                    y = Value.of(0)
                    x = Value.of(2 * PI + 20 / div)
                    ax = Value.of(-800)
                    ay = Value.of(0)
                }
                text { // arrow OY
                    x = Value.of(0)
                    y = Value.of(1 + 20 / div)
                    ax = Value.of(0)
                    ay = Value.of(430)
                }

                shape { // y = 1
                    xref = "x"
                    yref = "y"
                    startXY(-2 * PI, 1)
                    endXY(2 * PI, 1)
                    line { dash = Dash.dash }
                }
                shape { // y = 1/2
                    xref = "x"
                    yref = "y"
                    startXY(-2 * PI, 0.5)
                    endXY(2 * PI, 0.5)
                    line {
                        color("red")
                        dash = Dash.dash
                    }
                }
                shape { // y = -1
                    xref = "x"
                    yref = "y"
                    startXY(-2 * PI, -1)
                    endXY(2 * PI, -1)
                    line { dash = Dash.dash }
                }

                xaxis {
                    zeroline = false
                    showgrid = false
                    anchor = "free"
                    position = 0.43
                    tickvals(listOf(-2 * PI - 0.05, -PI - 0.15, PI - 0.05, 2 * PI + 0.1))
                    ticktext(listOf("\$\\huge{-2\\pi}\$", "\$\\huge{-\\pi}\$", "\$\\huge{\\pi}\$", "\$\\huge{2\\pi}\$"))
                }
                yaxis {
                    zeroline = false
                    showgrid = false
                    anchor = "free"
                    position = 0.485
                    tickvals(listOf(-0.91, 0.09, 1.09))
                    ticktext(listOf("\$\\Large{-1}\$", "\$\\Large{0}\$", "\$\\Large{1}\$"))
                }
                legend {
                    x = 0.97
                    y = 1
                    borderwidth = 1
                    font { size = 32 }
                    xanchor = XAnchor.right
                    yanchor = YAnchor.top
                }
            }
        }
    }.makeFile()
}