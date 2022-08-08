package space.kscience.plotly.jsdemo


import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.h1
import kotlinx.html.js.div
import kotlinx.html.style
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import space.kscience.plotly.*
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.models.TraceType
import kotlin.random.Random

private fun onDomLoaded(block: (Event) -> Unit) {
    document.addEventListener("DOMContentLoaded", block)
}

private fun withCanvas(block: TagConsumer<HTMLElement>.() -> Unit) = onDomLoaded {
    val element = document.getElementById("canvas") as? HTMLElement
        ?: error("Element with id 'app' not found on page")
    console.log("element loaded")
    element.append { block() }
}


@OptIn(DelicateCoroutinesApi::class)
fun main(): Unit = withCanvas {
    div {
        style = "height:50%; width=100%;"
        h1 { +"Histogram demo" }
        plotElement {
            val rnd = Random(222)
            histogram {
                name = "Random data"
                GlobalScope.launch {
                    while (isActive) {
                        x.numbers = List(500) { rnd.nextDouble() }
                        delay(300)
                    }
                }
            }

            layout {
                bargap = 0.1
                title {
                    text = "Basic Histogram"
                    font {
                        size = 20
                        color("black")
                    }
                }
                xaxis {
                    title {
                        text = "Value"
                        font {
                            size = 16
                        }
                    }
                }
                yaxis {
                    title {
                        text = "Count"
                        font {
                            size = 16
                        }
                    }
                }
            }
        }
    }

    div {
        style = "height:50%; width=100%;"
        h1 { +"Dynamic trace demo" }
        plotElement {
            scatter {
                x(1, 2, 3, 4)
                y(10, 15, 13, 17)
                mode = ScatterMode.markers
                type = TraceType.scatter
            }
            scatter {
                x(2, 3, 4, 5)
                y(10, 15, 13, 17)
                mode = ScatterMode.lines
                type = TraceType.scatter

                GlobalScope.launch {
                    while (isActive) {
                        delay(500)
                        marker {
                            if (Random.nextBoolean()) {
                                color("magenta")
                            } else {
                                color("blue")
                            }
                        }
                    }
                }
            }
            scatter {
                x(1, 2, 3, 4)
                y(12, 5, 2, 12)
                mode = ScatterMode.`lines+markers`
                type = TraceType.scatter
                marker {
                    color("red")
                }
            }
            layout {
                title = "Line and Scatter Plot"
            }
        }
    }
}


