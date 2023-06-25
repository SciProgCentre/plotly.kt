package space.kscience.plotly

import org.w3c.dom.Element
import org.w3c.dom.events.MouseEvent
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
@JsModule("plotly.js/dist/plotly")
@JsNonModule
public external object PlotlyJs {
    public fun newPlot(
        graphDiv: Element,
        data: Array<dynamic> = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    public fun react(
        graphDiv: Element,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally,
        config: dynamic = definedExternally
    )

    public fun update(
        graphDiv: Element,
        data: dynamic = definedExternally,
        layout: dynamic = definedExternally
    )

    public fun restyle(graphDiv: Element, update: dynamic, traceIndices: dynamic = definedExternally)
    public fun relayout(graphDiv: Element, update: dynamic)

    public fun toImage(root: Element, opts: ToImgOpts): Promise<String>
    public fun downloadImage(root: Element, opts: DownloadImgOpts): Promise<String>
}


public external interface PlotMouseEvent {
    public val points : Array<dynamic>
    public val event: MouseEvent
}
