package kscience.plotly.jsdemo


import hep.dataforge.meta.invoke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kscience.plotly.models.ScatterMode
import kscience.plotly.models.TraceType
import kscience.plotly.plot
import kscience.plotly.scatter
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.random.Random

fun main() {
    document.addEventListener("DOMContentLoaded", {
        val element =
            document.getElementById("canvas") as? HTMLElement ?: error("Element with id 'app' not found on page")

        console.log("element loaded")
        element.plot {
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
                marker {
                    GlobalScope.launch {
                        while (isActive) {
                            delay(500)
                            if(Random.nextBoolean()){
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

    })
}