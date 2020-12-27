package kscience.plotly

import org.w3c.dom.HTMLElement
import kotlin.js.Promise

public external interface ToImgOpts {
    public var format: String /* 'jpeg' | 'png' | 'webp' | 'svg' */
    public var width: Number
    public var height: Number
}

public external interface DownloadImgOpts {
    public var format: String /* 'jpeg' | 'png' | 'webp' | 'svg' */
    public var width: Number
    public var height: Number
    public var filename: String
}


@JsName("Plotly")
@JsModule("plotly.js/dist/plotly-basic")
@JsNonModule
public external object PlotlyJs {
    public fun newPlot(
        graphDiv: HTMLElement,
        data: Array<dynamic> = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    public fun react(
        graphDiv: HTMLElement,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    public fun update(
        graphDiv: HTMLElement,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally
    )

    public fun restyle(graphDiv: HTMLElement, update: dynamic, traceIndices: dynamic = definedExternally)
    public fun relayout(graphDiv: HTMLElement, update: dynamic)

    public fun toImage(root: HTMLElement, opts: ToImgOpts): Promise<String>
    public fun downloadImage(root: HTMLElement, opts: DownloadImgOpts): Promise<String>
}
