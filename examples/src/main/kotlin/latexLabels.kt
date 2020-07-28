import kotlinx.html.script
import kotlinx.html.unsafe
import scientifik.plotly.*

val customMathJaxHeader = HtmlFragment {
    script {
        type = "text/x-mathjax-config"
        unsafe {
            //language=JavaScript
            +"""
            MathJax.Hub.Config({
                tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
            });
            """
        }
    }
    script {
        type = "text/javascript"
        async = true
        src = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-MML-AM_SVG"
    }
}


fun main() {
    Plotly.page(customMathJaxHeader, cdnPlotlyHeader) {
        plot {
            scatter {
                x(2, 3, 4, 5)
                y(10, 15, 13, 17)
            }

            text {
                position(2, 10)
                font {
                    size = 18
                }
                text = "\$\\alpha\$"
            }

            text {
                position(5, 17)
                font {
                    size = 18
                }
                text = "\$\\Omega\$"
            }

            layout {
                title {
                    text = "Plot with annotations \$\\alpha~and~\\Omega\$"
                }
            }
        }
    }.makeFile()
}
