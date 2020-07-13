package scientifik.plotly

import org.w3c.dom.Element

@JsName("Plotly")
@JsModule("plotly.js/dist/plotly-basic")
@JsNonModule
external object PlotlyJs {
    fun newPlot(
        graphDiv: Element,
        data: Array<dynamic> = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    fun react(
        graphDiv: Element,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    fun update(
        graphDiv: Element,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally
    )

    fun restyle(graphDiv: Element, update: dynamic, traceIndices: dynamic = definedExternally)
    fun relayout(graphDiv: Element, update: dynamic)
}
