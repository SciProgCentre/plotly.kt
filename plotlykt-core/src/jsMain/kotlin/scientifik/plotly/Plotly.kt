package scientifik.plotly

import org.w3c.dom.Element

@JsName("Plotly")
@JsModule("plotly.js")
@JsNonModule
external object PlotlyJs {
    fun newPlot(
        graphDiv: Element,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    fun react(
        graphDiv: Element,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    fun restyle(graphDiv: Element, update: dynamic, traceIndices: dynamic = definedExternally)
    fun relayout(graphDiv: Element, update: dynamic)
}