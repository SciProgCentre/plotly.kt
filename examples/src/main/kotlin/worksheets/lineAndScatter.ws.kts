import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.models.ScatterMode
import scientifik.plotly.scatter
import scientifik.plotly.toHTML

Plotly.plot {
    scatter {
        x(1, 2, 3, 4)
        y(10, 15, 13, 17)
        mode = ScatterMode.markers
    }

    scatter {
        x(2, 3, 4, 5)
        y(10, 15, 13, 17)
        mode = ScatterMode.lines
    }

    scatter {
        x(1, 2, 3, 4)
        y(12, 5, 2, 12)
        mode = ScatterMode.`lines+markers`
    }

    layout {
        title = "Line and Scatter Plot"
    }
}.toHTML()