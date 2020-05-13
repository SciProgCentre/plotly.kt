import scientifik.plotly.Plotly
import scientifik.plotly.makeHtml
import scientifik.plotly.models.Mode
import scientifik.plotly.models.Type
import scientifik.plotly.trace

Plotly.plot2D {
    trace {
        x(1, 2, 3, 4)
        y(10, 15, 13, 17)
        mode = Mode.markers
        type = Type.scatter
    }
    trace {
        x(2, 3, 4, 5)
        y(10, 15, 13, 17)
        mode = Mode.lines
        type = Type.scatter
    }
    trace {
        x(1, 2, 3, 4)
        y(12, 5, 2, 12)
        mode = Mode.`lines+markers`
        type = Type.scatter
    }
    layout {
        title = "Line and Scatter Plot"
    }
}.makeHtml()