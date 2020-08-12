package kscience.plotly

import org.w3c.dom.HTMLElement
import kotlin.js.Promise

external interface ToImgOpts {
    var format: String /* 'jpeg' | 'png' | 'webp' | 'svg' */
    var width: Number
    var height: Number
}

external interface DownloadImgOpts {
    var format: String /* 'jpeg' | 'png' | 'webp' | 'svg' */
    var width: Number
    var height: Number
    var filename: String
}


@JsName("Plotly")
@JsModule("plotly.js/dist/plotly-basic")
@JsNonModule
external object PlotlyJs {
    fun newPlot(
        graphDiv: HTMLElement,
        data: Array<dynamic> = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    fun react(
        graphDiv: HTMLElement,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    fun update(
        graphDiv: HTMLElement,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally
    )

    fun restyle(graphDiv: HTMLElement, update: dynamic, traceIndices: dynamic = definedExternally)
    fun relayout(graphDiv: HTMLElement, update: dynamic)

    fun toImage(root: HTMLElement, opts: ToImgOpts): Promise<String>
    fun downloadImage(root: HTMLElement, opts: DownloadImgOpts): Promise<String>
}
