import hep.dataforge.meta.invoke
import scientifik.plotly.Plotly
import scientifik.plotly.makeHtml
import scientifik.plotly.models.TraceMode
import scientifik.plotly.models.TraceType
import scientifik.plotly.trace

Plotly.plot2D {
    trace {
        x(1, 2, 3, 4)
        y(10, 15, 13, 17)
        mode = TraceMode.markers
        type = TraceType.scatter
    }
    trace {
        x(2, 3, 4, 5)
        y(10, 15, 13, 17)
        mode = TraceMode.lines
        type = TraceType.scatter
    }
    trace {
        x(1, 2, 3, 4)
        y(12, 5, 2, 12)
        mode = TraceMode.`lines+markers`
        type = TraceType.scatter
    }
    layout {
        title { text = "Line and Scatter Plot" }
    }
}.makeHtml()