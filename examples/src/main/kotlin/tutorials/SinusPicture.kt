package tutorials

import kotlin.math.*
import scientifik.plotly.Plotly
import hep.dataforge.values.Value
import scientifik.plotly.*
import scientifik.plotly.models.*
import scientifik.plotly.palettes.XKCD


/**
 * - Draw Math Graph with annotations
 * - Custom zeroline
 * - Dashed and colored lines
 * - Draw vertical lines
 * - Use XKCD color palette
 * - Use LaTeX annotations
 * - Change margins on the plot edges
 */
fun main() {
    val div = 200 / PI
    val sub = PI / 6
    val x1 = (-410..410).map{ it / div }
    val y1 = mutableListOf<Double>()
    val yText = mutableListOf<String>()
    val y2 = MutableList(x1.size){1.0}
    val y3 = MutableList(x1.size){-1.0}
    val y4 = MutableList(x1.size){0.5}

    for (elem in x1) {
        val x = sin(elem)
        y1.add(x)
        yText.add("sin = $x")
    }

    val xAnnotation = Text {
        x = Value.of(0)
        y = Value.of(1 + 20 / div)
        ax = Value.of(0)
        ay = Value.of(430)
    }
    val yAnnotation = Text {
        y = Value.of(0)
        x = Value.of(2 * PI + 20 / div)
        ax = Value.of(-800)
        ay = Value.of(0)
    }
    val annotationsList = mutableListOf(xAnnotation, yAnnotation)

    Plotly.page(mathJaxHeader, cdnPlotlyHeader) {
        plot {
            scatter {
                x.set(x1)
                y.set(y1)
                name = "\$\\Large{y = \\mathrm{sin}\\,x}\$"
                text = yText
                hoverinfo = "text"
                line { color(XKCD.CERULEAN) }
            }

            scatter {
                x.set(x1)
                y.set(y2)
                line {
                    color(XKCD.DARK_GREY)
                    dash = Dash.dash
                }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                x.set(x1)
                y.set(y3)
                line {
                    color(XKCD.DARK_GREY)
                    dash = Dash.dash
                }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                x.set(x1)
                y.set(y4)
                line {
                    color("red")
                    dash = Dash.dash
                }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                mode = ScatterMode.`lines+markers`
                x(-PI * 2 + sub, -PI * 2 + sub)
                y(0, 0.5)
                line {
                    shape = LineShape.vh
                    color("red")
                }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                mode = ScatterMode.`lines+markers`
                x(-PI - sub, -PI - sub)
                y(0, 0.5)
                line {
                    shape = LineShape.vh
                    color("red")
                }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                mode = ScatterMode.`lines+markers`
                x(sub, sub)
                y(0, 0.5)
                line {
                    shape = LineShape.vh
                    color("red")
                }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                mode = ScatterMode.`lines+markers`
                x(PI - sub, PI - sub)
                y(0, 0.5)
                line {
                    shape = LineShape.vh
                    color("red")
                }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }
            scatter {
                mode = ScatterMode.markers
                x(-2* PI, -PI, PI, 2* PI)
                y(0, 0, 0, 0)
                line {
                    color(XKCD.CERULEAN_BLUE)
                }
                marker { size = 8 }
                showlegend = false
                hoverinfo = "none"
            }

            layout {
                height = 500
                width = 900
                annotations = annotationsList
                margin { l = 20; r = 20; b = 20; t = 50 }

                text {
                    x = Value.of(-0.285)
                    y = Value.of(0.56)
                    text = "\$\\Large{1/2}\$"
                    font {
                        color("red")
                    }
                    showarrow = false
                }
                text {
                    x = Value.of(0.28)
                    y = Value.of(1.3)
                    text = "\$\\huge{y}\$"
                    showarrow = false
                }
                text {
                    x = Value.of(PI * 2 + 0.4)
                    y = Value.of(0.1)
                    text = "\$\\huge{x}\$"
                    showarrow = false
                }

                xaxis {
                    zeroline = false
                    showgrid = false
                    anchor = "free"
                    position = 0.43
                    tickvals(listOf(-2 * PI-0.05, -PI - 0.15, PI - 0.05, 2 * PI + 0.1))
                    ticktext(listOf("\$\\huge{-2\\pi}\$", "\$\\huge{-\\pi}\$",
                            "\$\\huge{\\pi}\$", "\$\\huge{2\\pi}\$"))
                }
                yaxis {
                    zeroline = false
                    showgrid = false
                    anchor = "free"
                    position = 0.492
                    tickmode = TickMode.array
                    tickvals(listOf(-0.91, 0.09, 1.09))
                    ticktext(listOf("\$\\Large{-1}\$", "\$\\Large{0}\$", "\$\\Large{1}\$"))
                }
                legend {
                    x = 1.0
                    y = 1.0
                    borderwidth = 1
                    font {
                        size = 32
                    }
                    xanchor = XAnchor.right
                    yanchor = YAnchor.top
                }
            }
        }
    }.makeFile()
}