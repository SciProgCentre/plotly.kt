@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION")

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

external interface StaticPlots {
    fun resize(root: String)
    fun resize(root: HTMLElement)
}

external var Plots: StaticPlots

external interface Point {
    var x: Number
    var y: Number
    var z: Number
}

external interface PointPartial {
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var z: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlotScatterDataPoint {
    var curveNumber: Number
    var data: PlotData
    var pointIndex: Number
    var pointNumber: Number
    var x: Number
    var xaxis: LayoutAxis
    var y: Number
    var yaxis: LayoutAxis
}

external interface PlotDatum {
    var curveNumber: Number
    var data: PlotData
    var pointIndex: Number
    var pointNumber: Number
    var x: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var xaxis: LayoutAxis
    var y: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis: LayoutAxis
}

external interface PlotDatumPartial {
    var curveNumber: Number?
        get() = definedExternally
        set(value) = definedExternally
    var data: PlotData?
        get() = definedExternally
        set(value) = definedExternally
    var pointIndex: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pointNumber: Number?
        get() = definedExternally
        set(value) = definedExternally
    var x: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var xaxis: LayoutAxis?
        get() = definedExternally
        set(value) = definedExternally
    var y: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis: LayoutAxis?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlotMouseEvent {
    var points: Array<PlotDatum>
    var event: MouseEvent
}

external interface PlotCoordinate {
    var x: Number
    var y: Number
    var pointNumber: Number
}

external interface SelectionRange {
    var x: Array<Number>
    var y: Array<Number>
}

typealias PlotSelectedData = PlotDatumPartial

external interface PlotSelectionEvent {
    var points: Array<PlotDatum>
    var range: SelectionRange?
        get() = definedExternally
        set(value) = definedExternally
    var lassoPoints: SelectionRange?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlotAxis {
    var range: dynamic /* JsTuple<Number, Number> */
        get() = definedExternally
        set(value) = definedExternally
    var autorange: Boolean
}

external interface PlotScene {
    var center: Point
    var eye: Point
    var up: Point
}

external interface PlotRelayoutEvent {
    var xaxis: PlotAxis
    var yaxis: PlotAxis
    var scene: PlotScene
}

external interface ClickAnnotationEvent {
    var index: Number
    var annotation: Annotations
    var fullAnnotation: Annotations
    var event: MouseEvent
}

external interface `T$0` {
    var duration: Number
    var redraw: Boolean
}

external interface `T$1` {
    var frame: `T$0`
    var transition: Transition
}

external interface FrameAnimationEvent {
    var name: String
    var frame: Frame
    var animation: `T$1`
}

external interface LegendClickEvent {
    var event: MouseEvent
    var node: PlotlyHTMLElement
    var curveNumber: Number
    var expandedIndex: Number
    var data: Array<Data>
    var layout: LayoutPartial
    var frames: Array<Frame>
    var config: ConfigPartial
    var fullData: Array<Data>
    var fullLayout: LayoutPartial
}

external interface SliderChangeEvent {
    var slider: Slider
    var step: SliderStep
    var interaction: Boolean
    var previousActive: Number
}

external interface SliderStartEvent {
    var slider: Slider
}

external interface SliderEndEvent {
    var slider: Slider
    var step: SliderStep
}

external interface BeforePlotEvent {
    var data: Array<Data>
    var layout: LayoutPartial
    var config: ConfigPartial
}

external interface PlotlyHTMLElement : HTMLElement {
    fun on(event: String, callback: (event: PlotMouseEvent) -> Unit)
    fun on(event: String, callback: (event: PlotSelectionEvent) -> Unit)
    fun on(event: String /* 'plotly_restyle' */, callback: (data: dynamic /* JsTuple<Any, Array<Number>> */) -> Unit)
    fun on(event: String /* 'plotly_relayout' */, callback: (event: PlotRelayoutEvent) -> Unit)
    fun on(event: String /* 'plotly_clickannotation' */, callback: (event: ClickAnnotationEvent) -> Unit)
    fun on(event: String /* 'plotly_animatingframe' */, callback: (event: FrameAnimationEvent) -> Unit)
    fun on(event: String, callback: (event: LegendClickEvent) -> Boolean)
    fun on(event: String /* 'plotly_sliderchange' */, callback: (event: SliderChangeEvent) -> Unit)
    fun on(event: String /* 'plotly_sliderend' */, callback: (event: SliderEndEvent) -> Unit)
    fun on(event: String /* 'plotly_sliderstart' */, callback: (event: SliderStartEvent) -> Unit)
    fun on(event: String /* 'plotly_event' */, callback: (data: Any) -> Unit)
    fun on(event: String /* 'plotly_beforeplot' */, callback: (event: BeforePlotEvent) -> Boolean)
    fun on(event: String, callback: () -> Unit)
    var removeAllListeners: (handler: String) -> Unit
}

external interface ToImgopts {
    var format: String /* 'jpeg' | 'png' | 'webp' | 'svg' */
    var width: Number
    var height: Number
}

external interface DownloadImgopts {
    var format: String /* 'jpeg' | 'png' | 'webp' | 'svg' */
    var width: Number
    var height: Number
    var filename: String
}

external interface PolarLayout {
    var domain: DomainPartial
    var sector: Array<Number>
    var hole: Number
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var radialaxis: LayoutAxisPartial
    var angularaxis: LayoutAxisPartial
    var gridshape: String /* 'circular' | 'linear' */
    var uirevision: dynamic /* String | Number */
        get() = definedExternally
        set(value) = definedExternally
}

external interface PolarLayoutPartial {
    var domain: Any?
        get() = definedExternally
        set(value) = definedExternally
    var sector: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var hole: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var radialaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var angularaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var gridshape: String /* 'circular' | 'linear' */
    var uirevision: dynamic /* String | Number */
        get() = definedExternally
        set(value) = definedExternally
}

external fun newPlot(root: String, data: Array<Data>, layout: LayoutPartial? = definedExternally, config: ConfigPartial? = definedExternally): Promise<PlotlyHTMLElement>

external fun newPlot(root: HTMLElement, data: Array<Data>, layout: LayoutPartial? = definedExternally, config: ConfigPartial? = definedExternally): Promise<PlotlyHTMLElement>

external fun plot(root: String, data: Array<Data>, layout: LayoutPartial? = definedExternally, config: ConfigPartial? = definedExternally): Promise<PlotlyHTMLElement>

external fun plot(root: HTMLElement, data: Array<Data>, layout: LayoutPartial? = definedExternally, config: ConfigPartial? = definedExternally): Promise<PlotlyHTMLElement>

external fun relayout(root: String, layout: LayoutPartial): Promise<PlotlyHTMLElement>

external fun relayout(root: HTMLElement, layout: LayoutPartial): Promise<PlotlyHTMLElement>

external fun redraw(root: String): Promise<PlotlyHTMLElement>

external fun redraw(root: HTMLElement): Promise<PlotlyHTMLElement>

external fun purge(root: String)

external fun purge(root: HTMLElement)

external var d3: Any

external fun restyle(root: String, aobj: Data, traces: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun restyle(root: String, aobj: Data, traces: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun restyle(root: HTMLElement, aobj: Data, traces: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun restyle(root: HTMLElement, aobj: Data, traces: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun update(root: String, traceUpdate: Data, layoutUpdate: LayoutPartial, traces: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun update(root: String, traceUpdate: Data, layoutUpdate: LayoutPartial, traces: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun update(root: HTMLElement, traceUpdate: Data, layoutUpdate: LayoutPartial, traces: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun update(root: HTMLElement, traceUpdate: Data, layoutUpdate: LayoutPartial, traces: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: String, traces: Data, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: String, traces: Data, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: String, traces: Array<Data>, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: String, traces: Array<Data>, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: HTMLElement, traces: Data, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: HTMLElement, traces: Data, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: HTMLElement, traces: Array<Data>, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun addTraces(root: HTMLElement, traces: Array<Data>, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun deleteTraces(root: String, indices: Array<Number>): Promise<PlotlyHTMLElement>

external fun deleteTraces(root: String, indices: Number): Promise<PlotlyHTMLElement>

external fun deleteTraces(root: HTMLElement, indices: Array<Number>): Promise<PlotlyHTMLElement>

external fun deleteTraces(root: HTMLElement, indices: Number): Promise<PlotlyHTMLElement>

external fun moveTraces(root: String, currentIndices: Array<Number>, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: String, currentIndices: Array<Number>, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: String, currentIndices: Number, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: String, currentIndices: Number, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: HTMLElement, currentIndices: Array<Number>, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: HTMLElement, currentIndices: Array<Number>, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: HTMLElement, currentIndices: Number, newIndices: Array<Number>? = definedExternally): Promise<PlotlyHTMLElement>

external fun moveTraces(root: HTMLElement, currentIndices: Number, newIndices: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: String, update: Data, indices: Number, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: String, update: Data, indices: Array<Number>, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: String, update: Array<Data>, indices: Number, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: String, update: Array<Data>, indices: Array<Number>, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: HTMLElement, update: Data, indices: Number, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: HTMLElement, update: Data, indices: Array<Number>, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: HTMLElement, update: Array<Data>, indices: Number, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun extendTraces(root: HTMLElement, update: Array<Data>, indices: Array<Number>, maxPoints: Number? = definedExternally): Promise<PlotlyHTMLElement>

external fun prependTraces(root: String, update: Data, indices: Number): Promise<PlotlyHTMLElement>

external fun prependTraces(root: String, update: Data, indices: Array<Number>): Promise<PlotlyHTMLElement>

external fun prependTraces(root: String, update: Array<Data>, indices: Number): Promise<PlotlyHTMLElement>

external fun prependTraces(root: String, update: Array<Data>, indices: Array<Number>): Promise<PlotlyHTMLElement>

external fun prependTraces(root: HTMLElement, update: Data, indices: Number): Promise<PlotlyHTMLElement>

external fun prependTraces(root: HTMLElement, update: Data, indices: Array<Number>): Promise<PlotlyHTMLElement>

external fun prependTraces(root: HTMLElement, update: Array<Data>, indices: Number): Promise<PlotlyHTMLElement>

external fun prependTraces(root: HTMLElement, update: Array<Data>, indices: Array<Number>): Promise<PlotlyHTMLElement>

external fun toImage(root: String, opts: ToImgopts): Promise<String>

external fun toImage(root: HTMLElement, opts: ToImgopts): Promise<String>

external fun downloadImage(root: String, opts: DownloadImgopts): Promise<String>

external fun downloadImage(root: HTMLElement, opts: DownloadImgopts): Promise<String>

external fun react(root: String, data: Array<Data>, layout: LayoutPartial? = definedExternally, config: ConfigPartial? = definedExternally): Promise<PlotlyHTMLElement>

external fun react(root: HTMLElement, data: Array<Data>, layout: LayoutPartial? = definedExternally, config: ConfigPartial? = definedExternally): Promise<PlotlyHTMLElement>

external fun addFrames(root: String, frames: Array<FramePartial>): Promise<PlotlyHTMLElement>

external fun addFrames(root: HTMLElement, frames: Array<FramePartial>): Promise<PlotlyHTMLElement>

external fun deleteFrames(root: String, frames: Array<Number>): Promise<PlotlyHTMLElement>

external fun deleteFrames(root: HTMLElement, frames: Array<Number>): Promise<PlotlyHTMLElement>

external interface `T$2` {
    var text: String?
        get() = definedExternally
        set(value) = definedExternally
    var font: FontPartial?
        get() = definedExternally
        set(value) = definedExternally
    var xref: String /* 'container' | 'paper' */
    var yref: String /* 'container' | 'paper' */
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var pad: PaddingPartial?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$3` {
    var x: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var y: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$4` {
    var rows: Number?
        get() = definedExternally
        set(value) = definedExternally
    var roworder: String /* "top to bottom" | "bottom to top" */
    var columns: Number?
        get() = definedExternally
        set(value) = definedExternally
    var subplots: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var xaxes: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var yaxes: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var pattern: String /* "independent" | "coupled" */
    var xgap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ygap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var domain: `T$3`?
        get() = definedExternally
        set(value) = definedExternally
    var xside: String /* "bottom" | "bottom plot" | "top plot" | "top" */
    var yside: String /* "left" | "left plot" | "right plot" | "right" */
}

external interface Layout {
    var title: dynamic /* String | `T$2` */
        get() = definedExternally
        set(value) = definedExternally
    var titlefont: FontPartial
    var autosize: Boolean
    var showlegend: Boolean
    var paper_bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var plot_bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var separators: String
    var hidesources: Boolean
    var xaxis: LayoutAxisPartial
    var xaxis2: LayoutAxisPartial
    var xaxis3: LayoutAxisPartial
    var xaxis4: LayoutAxisPartial
    var xaxis5: LayoutAxisPartial
    var xaxis6: LayoutAxisPartial
    var xaxis7: LayoutAxisPartial
    var xaxis8: LayoutAxisPartial
    var xaxis9: LayoutAxisPartial
    var yaxis: LayoutAxisPartial
    var yaxis2: LayoutAxisPartial
    var yaxis3: LayoutAxisPartial
    var yaxis4: LayoutAxisPartial
    var yaxis5: LayoutAxisPartial
    var yaxis6: LayoutAxisPartial
    var yaxis7: LayoutAxisPartial
    var yaxis8: LayoutAxisPartial
    var yaxis9: LayoutAxisPartial
    var margin: MarginPartial
    var height: Number
    var width: Number
    var hovermode: dynamic /* 'closest' | 'x' | 'y' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var hoverlabel: HoverLabelPartial
    var calendar: String /* 'gregorian' | 'chinese' | 'coptic' | 'discworld' | 'ethiopian' | 'hebrew' | 'islamic' | 'julian' | 'mayan' | 'nanakshahi' | 'nepali' | 'persian' | 'jalali' | 'taiwan' | 'thai' | 'ummalqura' */
    var xaxis.range: dynamic /* JsTuple<dynamic, dynamic> */
        get() = definedExternally
        set(value) = definedExternally
    var xaxis.range[0]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var xaxis.range[1]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.range: dynamic /* JsTuple<dynamic, dynamic> */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.range[0]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.range[1]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.type: String /* '-' | 'linear' | 'log' | 'date' | 'category' */
    var xaxis.type: String /* '-' | 'linear' | 'log' | 'date' | 'category' */
    var xaxis.autorange: Boolean
    var yaxis.autorange: Boolean
    var xaxis.title: String
    var yaxis.title: String
    var ternary: Any
    var geo: Any
    var mapbox: Any
    var radialaxis: AxisPartial
    var angularaxis: Any
    var dragmode: dynamic /* 'zoom' | 'pan' | 'select' | 'lasso' | 'orbit' | 'turntable' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var orientation: Number
    var annotations: Array<AnnotationsPartial>
    var shapes: Array<ShapePartial>
    var images: Array<ImagePartial>
    var updatemenus: Any
    var sliders: Array<SliderPartial>
    var legend: LegendPartial
    var font: FontPartial
    var scene: ScenePartial
    var barmode: String /* 'stack' | 'group' | 'overlay' | 'relative' */
    var barnorm: String /* '' | 'fraction' | 'percent' */
    var bargap: String /* 0 | 1 */
    var bargroupgap: String /* 0 | 1 */
    var selectdirection: String /* 'h' | 'v' | 'd' | 'any' */
    var hiddenlabels: Array<String>
    var grid: `T$4`
    var polar: PolarLayoutPartial
    var polar2: PolarLayoutPartial
    var polar3: PolarLayoutPartial
    var polar4: PolarLayoutPartial
    var polar5: PolarLayoutPartial
    var polar6: PolarLayoutPartial
    var polar7: PolarLayoutPartial
    var polar8: PolarLayoutPartial
    var polar9: PolarLayoutPartial
    var transition: Transition
}

external interface LayoutPartial {
    var title: dynamic /* String | `T$2` */
        get() = definedExternally
        set(value) = definedExternally
    var titlefont: Any?
        get() = definedExternally
        set(value) = definedExternally
    var autosize: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showlegend: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var paper_bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var plot_bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var separators: String?
        get() = definedExternally
        set(value) = definedExternally
    var hidesources: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis2: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis3: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis4: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis5: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis6: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis7: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis8: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis9: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis2: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis3: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis4: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis5: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis6: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis7: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis8: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis9: Any?
        get() = definedExternally
        set(value) = definedExternally
    var margin: Any?
        get() = definedExternally
        set(value) = definedExternally
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var hovermode: dynamic /* 'closest' | 'x' | 'y' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var hoverlabel: Any?
        get() = definedExternally
        set(value) = definedExternally
    var calendar: String /* 'gregorian' | 'chinese' | 'coptic' | 'discworld' | 'ethiopian' | 'hebrew' | 'islamic' | 'julian' | 'mayan' | 'nanakshahi' | 'nepali' | 'persian' | 'jalali' | 'taiwan' | 'thai' | 'ummalqura' */
    var xaxis.range: dynamic /* JsTuple<dynamic, dynamic> */
        get() = definedExternally
        set(value) = definedExternally
    var xaxis.range[0]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var xaxis.range[1]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.range: dynamic /* JsTuple<dynamic, dynamic> */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.range[0]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.range[1]: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.type: String /* '-' | 'linear' | 'log' | 'date' | 'category' */
    var xaxis.type: String /* '-' | 'linear' | 'log' | 'date' | 'category' */
    var xaxis.autorange: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.autorange: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis.title: String?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis.title: String?
        get() = definedExternally
        set(value) = definedExternally
    var ternary: Any?
        get() = definedExternally
        set(value) = definedExternally
    var geo: Any?
        get() = definedExternally
        set(value) = definedExternally
    var mapbox: Any?
        get() = definedExternally
        set(value) = definedExternally
    var radialaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var angularaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var dragmode: dynamic /* 'zoom' | 'pan' | 'select' | 'lasso' | 'orbit' | 'turntable' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var orientation: Number?
        get() = definedExternally
        set(value) = definedExternally
    var annotations: Array<Partial<Annotations>>?
        get() = definedExternally
        set(value) = definedExternally
    var shapes: Array<Partial<Shape>>?
        get() = definedExternally
        set(value) = definedExternally
    var images: Array<Partial<Image>>?
        get() = definedExternally
        set(value) = definedExternally
    var updatemenus: Any?
        get() = definedExternally
        set(value) = definedExternally
    var sliders: Array<Partial<Slider>>?
        get() = definedExternally
        set(value) = definedExternally
    var legend: Any?
        get() = definedExternally
        set(value) = definedExternally
    var font: Any?
        get() = definedExternally
        set(value) = definedExternally
    var scene: Any?
        get() = definedExternally
        set(value) = definedExternally
    var barmode: String /* 'stack' | 'group' | 'overlay' | 'relative' */
    var barnorm: String /* '' | 'fraction' | 'percent' */
    var bargap: String /* 0 | 1 */
    var bargroupgap: String /* 0 | 1 */
    var selectdirection: String /* 'h' | 'v' | 'd' | 'any' */
    var hiddenlabels: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var grid: `T$4`?
        get() = definedExternally
        set(value) = definedExternally
    var polar: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar2: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar3: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar4: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar5: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar6: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar7: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar8: Any?
        get() = definedExternally
        set(value) = definedExternally
    var polar9: Any?
        get() = definedExternally
        set(value) = definedExternally
    var transition: Transition?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Legend : Label {
    var traceorder: String /* 'grouped' | 'normal' | 'reversed' */
    var x: Number
    var y: Number
    var borderwidth: Number
    var orientation: String /* 'v' | 'h' */
    var tracegroupgap: Number
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
}

external interface LegendPartial : LabelPartial {
    var traceorder: String /* 'grouped' | 'normal' | 'reversed' */
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var orientation: String /* 'v' | 'h' */
    var tracegroupgap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
}

external interface Axis {
    var visible: Boolean
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var title: dynamic /* String | DataTitlePartial */
        get() = definedExternally
        set(value) = definedExternally
    var titlefont: FontPartial
    var type: String /* '-' | 'linear' | 'log' | 'date' | 'category' */
    var autorange: dynamic /* Boolean | 'reversed' */
        get() = definedExternally
        set(value) = definedExternally
    var rangemode: String /* 'normal' | 'tozero' | 'nonnegative' */
    var range: Array<Any>
    var tickmode: String /* 'auto' | 'linear' | 'array' */
    var nticks: Number
    var tick0: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var dtick: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var tickvals: Array<Any>
    var ticktext: Array<String>
    var ticks: String /* 'outside' | 'inside' | '' */
    var mirror: dynamic /* Boolean | 'ticks' | 'all' | 'allticks' */
        get() = definedExternally
        set(value) = definedExternally
    var ticklen: Number
    var tickwidth: Number
    var tickcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var showticklabels: Boolean
    var showspikes: Boolean
    var spikecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var spikethickness: Number
    var categoryorder: String /* 'trace' | 'category ascending' | 'category descending' | 'array' | 'total ascending' | 'total descending' | 'min ascending' | 'min descending' | 'max ascending' | 'max descending' | 'sum ascending' | 'sum descending' | 'mean ascending' | 'mean descending' | 'median ascending' | 'median descending' */
    var categoryarray: Array<Any>
    var tickfont: FontPartial
    var tickangle: Number
    var tickprefix: String
    var showtickprefix: String /* 'all' | 'first' | 'last' | 'none' */
    var ticksuffix: String
    var showticksuffix: String /* 'all' | 'first' | 'last' | 'none' */
    var showexponent: String /* 'all' | 'first' | 'last' | 'none' */
    var exponentformat: String /* 'none' | 'e' | 'E' | 'power' | 'SI' | 'B' */
    var separatethousands: Boolean
    var tickformat: String
    var hoverformat: String
    var showline: Boolean
    var linecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var linewidth: Number
    var showgrid: Boolean
    var gridcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var gridwidth: Number
    var zeroline: Boolean
    var zerolinecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var zerolinewidth: Number
    var calendar: String /* 'gregorian' | 'chinese' | 'coptic' | 'discworld' | 'ethiopian' | 'hebrew' | 'islamic' | 'julian' | 'mayan' | 'nanakshahi' | 'nepali' | 'persian' | 'jalali' | 'taiwan' | 'thai' | 'ummalqura' */
}

external interface AxisPartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var title: dynamic /* String | Partial<DataTitle> */
        get() = definedExternally
        set(value) = definedExternally
    var titlefont: Any?
        get() = definedExternally
        set(value) = definedExternally
    var type: String /* '-' | 'linear' | 'log' | 'date' | 'category' */
    var autorange: dynamic /* Boolean | 'reversed' */
        get() = definedExternally
        set(value) = definedExternally
    var rangemode: String /* 'normal' | 'tozero' | 'nonnegative' */
    var range: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var tickmode: String /* 'auto' | 'linear' | 'array' */
    var nticks: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tick0: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var dtick: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var tickvals: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var ticktext: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var ticks: String /* 'outside' | 'inside' | '' */
    var mirror: dynamic /* Boolean | 'ticks' | 'all' | 'allticks' */
        get() = definedExternally
        set(value) = definedExternally
    var ticklen: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var showticklabels: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showspikes: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var spikecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var spikethickness: Number?
        get() = definedExternally
        set(value) = definedExternally
    var categoryorder: String /* 'trace' | 'category ascending' | 'category descending' | 'array' | 'total ascending' | 'total descending' | 'min ascending' | 'min descending' | 'max ascending' | 'max descending' | 'sum ascending' | 'sum descending' | 'mean ascending' | 'mean descending' | 'median ascending' | 'median descending' */
    var categoryarray: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var tickfont: Any?
        get() = definedExternally
        set(value) = definedExternally
    var tickangle: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickprefix: String?
        get() = definedExternally
        set(value) = definedExternally
    var showtickprefix: String /* 'all' | 'first' | 'last' | 'none' */
    var ticksuffix: String?
        get() = definedExternally
        set(value) = definedExternally
    var showticksuffix: String /* 'all' | 'first' | 'last' | 'none' */
    var showexponent: String /* 'all' | 'first' | 'last' | 'none' */
    var exponentformat: String /* 'none' | 'e' | 'E' | 'power' | 'SI' | 'B' */
    var separatethousands: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var tickformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var hoverformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var showline: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var linecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var linewidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var showgrid: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var gridcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var gridwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var zeroline: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var zerolinecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var zerolinewidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var calendar: String /* 'gregorian' | 'chinese' | 'coptic' | 'discworld' | 'ethiopian' | 'hebrew' | 'islamic' | 'julian' | 'mayan' | 'nanakshahi' | 'nepali' | 'persian' | 'jalali' | 'taiwan' | 'thai' | 'ummalqura' */
}

external interface LayoutAxis : Axis {
    var fixedrange: Boolean
    var scaleanchor: String /* 'x' | 'x2' | 'x3' | 'x4' | 'x5' | 'x6' | 'x7' | 'x8' | 'x9' | 'y' | 'y2' | 'y3' | 'y4' | 'y5' | 'y6' | 'y7' | 'y8' | 'y9' */
    var scaleratio: Number
    var constrain: String /* 'range' | 'domain' */
    var constraintoward: String /* 'left' | 'center' | 'right' | 'top' | 'middle' | 'bottom' */
    var spikedash: String
    var spikemode: String
    var anchor: String /* 'free' | 'x' | 'x2' | 'x3' | 'x4' | 'x5' | 'x6' | 'x7' | 'x8' | 'x9' | 'y' | 'y2' | 'y3' | 'y4' | 'y5' | 'y6' | 'y7' | 'y8' | 'y9' */
    var side: String /* 'top' | 'bottom' | 'left' | 'right' | 'clockwise' | 'counterclockwise' */
    var overlaying: String /* 'free' | 'x' | 'x2' | 'x3' | 'x4' | 'x5' | 'x6' | 'x7' | 'x8' | 'x9' | 'y' | 'y2' | 'y3' | 'y4' | 'y5' | 'y6' | 'y7' | 'y8' | 'y9' */
    var layer: String /* 'above traces' | 'below traces' */
    var domain: Array<Number>
    var position: Number
    var rangeslider: RangeSliderPartial
    var rangeselector: RangeSelectorPartial
    var automargin: Boolean
    var autotick: Boolean
    var angle: Any
}

external interface LayoutAxisPartial : AxisPartial {
    var fixedrange: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var scaleanchor: String /* 'x' | 'x2' | 'x3' | 'x4' | 'x5' | 'x6' | 'x7' | 'x8' | 'x9' | 'y' | 'y2' | 'y3' | 'y4' | 'y5' | 'y6' | 'y7' | 'y8' | 'y9' */
    var scaleratio: Number?
        get() = definedExternally
        set(value) = definedExternally
    var constrain: String /* 'range' | 'domain' */
    var constraintoward: String /* 'left' | 'center' | 'right' | 'top' | 'middle' | 'bottom' */
    var spikedash: String?
        get() = definedExternally
        set(value) = definedExternally
    var spikemode: String?
        get() = definedExternally
        set(value) = definedExternally
    var anchor: String /* 'free' | 'x' | 'x2' | 'x3' | 'x4' | 'x5' | 'x6' | 'x7' | 'x8' | 'x9' | 'y' | 'y2' | 'y3' | 'y4' | 'y5' | 'y6' | 'y7' | 'y8' | 'y9' */
    var side: String /* 'top' | 'bottom' | 'left' | 'right' | 'clockwise' | 'counterclockwise' */
    var overlaying: String /* 'free' | 'x' | 'x2' | 'x3' | 'x4' | 'x5' | 'x6' | 'x7' | 'x8' | 'x9' | 'y' | 'y2' | 'y3' | 'y4' | 'y5' | 'y6' | 'y7' | 'y8' | 'y9' */
    var layer: String /* 'above traces' | 'below traces' */
    var domain: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var position: Number?
        get() = definedExternally
        set(value) = definedExternally
    var rangeslider: Any?
        get() = definedExternally
        set(value) = definedExternally
    var rangeselector: Any?
        get() = definedExternally
        set(value) = definedExternally
    var automargin: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autotick: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var angle: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SceneAxis : Axis {
    var spikesides: Boolean
    var showbackground: Boolean
    var backgroundcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var showaxeslabels: Boolean
}

external interface SceneAxisPartial : AxisPartial {
    var spikesides: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showbackground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var backgroundcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var showaxeslabels: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ShapeLine {
    var color: String
    var width: Number
    var dash: String /* 'solid' | 'dot' | 'dash' | 'longdash' | 'dashdot' | 'longdashdot' */
}

external interface ShapeLinePartial {
    var color: String?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var dash: String /* 'solid' | 'dot' | 'dash' | 'longdash' | 'dashdot' | 'longdashdot' */
}

external interface Shape {
    var visible: Boolean
    var layer: String /* 'below' | 'above' */
    var type: String /* 'rect' | 'circle' | 'line' | 'path' */
    var path: String
    var xref: String /* 'x' | 'paper' */
    var xsizemode: String /* "scaled" | "pixel" */
    var xanchor: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var yref: String /* 'paper' | 'y' */
    var ysizemode: String /* "scaled" | "pixel" */
    var yanchor: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var x0: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var y0: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var x1: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var y1: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var fillcolor: String
    var name: String
    var templateitemname: String
    var opacity: Number
    var line: ShapeLinePartial
}

external interface ShapePartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var layer: String /* 'below' | 'above' */
    var type: String /* 'rect' | 'circle' | 'line' | 'path' */
    var path: String?
        get() = definedExternally
        set(value) = definedExternally
    var xref: String /* 'x' | 'paper' */
    var xsizemode: String /* "scaled" | "pixel" */
    var xanchor: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var yref: String /* 'paper' | 'y' */
    var ysizemode: String /* "scaled" | "pixel" */
    var yanchor: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var x0: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var y0: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var x1: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var y1: dynamic /* String | Number | Date | Nothing? */
        get() = definedExternally
        set(value) = definedExternally
    var fillcolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var templateitemname: String?
        get() = definedExternally
        set(value) = definedExternally
    var opacity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var line: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Margin {
    var t: Number
    var b: Number
    var l: Number
    var r: Number
    var pad: Number
}

external interface MarginPartial {
    var t: Number?
        get() = definedExternally
        set(value) = definedExternally
    var b: Number?
        get() = definedExternally
        set(value) = definedExternally
    var l: Number?
        get() = definedExternally
        set(value) = definedExternally
    var r: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pad: Number?
        get() = definedExternally
        set(value) = definedExternally
}

typealias ButtonClickEvent = (gd: PlotlyHTMLElement, ev: MouseEvent) -> Unit

external interface Icon {
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ascent: Number?
        get() = definedExternally
        set(value) = definedExternally
    var descent: Number?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var path: String?
        get() = definedExternally
        set(value) = definedExternally
    var svg: String?
        get() = definedExternally
        set(value) = definedExternally
    var transform: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ModeBarButton {
    var name: String
    var title: String
    var icon: dynamic /* String | Icon */
        get() = definedExternally
        set(value) = definedExternally
    var gravity: String?
        get() = definedExternally
        set(value) = definedExternally
    var click: ButtonClickEvent
    var attr: String?
        get() = definedExternally
        set(value) = definedExternally
    var `val`: Any?
        get() = definedExternally
        set(value) = definedExternally
    var toggle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface GaugeLine {
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var width: Number
}

external interface GaugeLinePartial {
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Threshold {
    var line: GaugeLinePartial
    var value: Number
    var thickness: Number
}

external interface ThresholdPartial {
    var line: Any?
        get() = definedExternally
        set(value) = definedExternally
    var value: Number?
        get() = definedExternally
        set(value) = definedExternally
    var thickness: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface GaugeBar {
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var line: GaugeLinePartial
    var thickness: Number
}

external interface GaugeBarPartial {
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var line: Any?
        get() = definedExternally
        set(value) = definedExternally
    var thickness: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$5` {
    var range: Array<Number>
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Gauge {
    var shape: String /* 'angular' | 'bullet' */
    var bar: GaugeBarPartial
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number
    var axis: AxisPartial
    var steps: Array<`T$5`>
    var threshold: ThresholdPartial
}

external interface GaugePartial {
    var shape: String /* 'angular' | 'bullet' */
    var bar: Any?
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var axis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var steps: Array<`T$5`>?
        get() = definedExternally
        set(value) = definedExternally
    var threshold: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$6` {
    var symbol: String
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Delta {
    var reference: Number
    var position: String /* 'top' | 'bottom' | 'left' | 'right' */
    var relative: Boolean
    var valueformat: String
    var increasing: `T$6`
    var decreasing: `T$6`
}

external interface DeltaPartial {
    var reference: Number?
        get() = definedExternally
        set(value) = definedExternally
    var position: String /* 'top' | 'bottom' | 'left' | 'right' */
    var relative: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var valueformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var increasing: `T$6`?
        get() = definedExternally
        set(value) = definedExternally
    var decreasing: `T$6`?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DataTitle {
    var text: String
    var font: FontPartial
    var position: String /* "top left" | "top center" | "top right" | "middle center" | "bottom left" | "bottom center" | "bottom right" */
}

external interface DataTitlePartial {
    var text: String?
        get() = definedExternally
        set(value) = definedExternally
    var font: Any?
        get() = definedExternally
        set(value) = definedExternally
    var position: String /* "top left" | "top center" | "top right" | "middle center" | "bottom left" | "bottom center" | "bottom right" */
}

external interface PlotNumber {
    var valueformat: String
    var font: FontPartial
    var prefix: String
    var suffix: String
}

external interface PlotNumberPartial {
    var valueformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var font: Any?
        get() = definedExternally
        set(value) = definedExternally
    var prefix: String?
        get() = definedExternally
        set(value) = definedExternally
    var suffix: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ErrorOptions {
    var visible: Boolean
    var symmetric: Boolean
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var thickness: Number
    var width: Number
    var opacity: Number
}

external interface ErrorOptionsPartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var symmetric: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var thickness: Number?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var opacity: Number?
        get() = definedExternally
        set(value) = definedExternally
}

typealias Data = PlotDataPartial

typealias DataTransform = TransformPartial

typealias ScatterData = PlotData

external interface `T$7` {
    var start: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var end: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var size: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$8` {
    var rows: Number?
        get() = definedExternally
        set(value) = definedExternally
    var columns: Number?
        get() = definedExternally
        set(value) = definedExternally
    var x: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var y: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlotData {
    var type: String /* 'bar' | 'box' | 'candlestick' | 'choropleth' | 'contour' | 'heatmap' | 'histogram' | 'indicator' | 'mesh3d' | 'ohlc' | 'parcoords' | 'pie' | 'pointcloud' | 'scatter' | 'scatter3d' | 'scattergeo' | 'scattergl' | 'scatterpolar' | 'scatterternary' | 'surface' | 'treemap' | 'waterfall' | 'funnel' | 'funnelarea' */
    var x: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var y: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var z: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Array<Array<Array<dynamic /* String | Number | Date | Nothing? */>>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var xy: Float32Array
    var error_x: ErrorOptionsPartial /* ErrorOptionsPartial & dynamic */
    var error_y: ErrorOptionsPartial /* ErrorOptionsPartial & dynamic */
    var xaxis: String
    var yaxis: String
    var text: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var line: ScatterLinePartial
    var line.color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var line.width: Number
    var line.dash: String /* 'solid' | 'dot' | 'dash' | 'longdash' | 'dashdot' | 'longdashdot' */
    var line.shape: String /* 'linear' | 'spline' | 'hv' | 'vh' | 'hvh' | 'vhv' */
    var line.smoothing: Number
    var line.simplify: Boolean
    var marker: PlotMarkerPartial
    var marker.symbol: dynamic /* String | Number | Array<dynamic /* String | Number */> | Array<dynamic /* String | Number | Array<dynamic /* String | Number */> */> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> | Array<dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.opacity: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.size: dynamic /* Number | Array<Number> | Array<Array<Number>> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.maxdisplayed: Number
    var marker.sizeref: Number
    var marker.sizemax: Number
    var marker.sizemin: Number
    var marker.sizemode: String /* 'diameter' | 'area' */
    var marker.showscale: Boolean
    var marker.line: ScatterMarkerLinePartial
    var marker.line.color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.line.colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> | Array<dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.colorbar: Any
    var marker.pad.t: Number
    var marker.pad.b: Number
    var marker.pad.l: Number
    var marker.pad.r: Number
    var mode: String /* 'lines' | 'markers' | 'text' | 'lines+markers' | 'text+markers' | 'text+lines' | 'text+lines+markers' | 'none' | 'gauge' | 'number' | 'delta' | 'number+delta' | 'gauge+number' | 'gauge+number+delta' | 'gauge+delta' */
    var hoveron: String /* 'points' | 'fills' */
    var hoverinfo: String /* 'all' | 'name' | 'none' | 'skip' | 'text' | 'x' | 'x+text' | 'x+name' | 'x+y' | 'x+y+text' | 'x+y+name' | 'x+y+z' | 'x+y+z+text' | 'x+y+z+name' | 'y+name' | 'y+x' | 'y+text' | 'y+x+text' | 'y+x+name' | 'y+z' | 'y+z+text' | 'y+z+name' | 'y+x+z' | 'y+x+z+text' | 'y+x+z+name' | 'z+x' | 'z+x+text' | 'z+x+name' | 'z+y+x' | 'z+y+x+text' | 'z+y+x+name' | 'z+x+y' | 'z+x+y+text' | 'z+x+y+name' */
    var hoverlabel: HoverLabelPartial
    var hovertemplate: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var hovertext: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var textinfo: String /* 'label' | 'label+text' | 'label+value' | 'label+percent' | 'label+text+value' | 'label+text+percent' | 'label+value+percent' | 'text' | 'text+value' | 'text+percent' | 'text+value+percent' | 'value' | 'value+percent' | 'percent' | 'none' */
    var textposition: String /* "top left" | "top center" | "top right" | "middle left" | "middle center" | "middle right" | "bottom left" | "bottom center" | "bottom right" | "inside" | "outside" */
    var textfont: FontPartial
    var fill: String /* 'none' | 'tozeroy' | 'tozerox' | 'tonexty' | 'tonextx' | 'toself' | 'tonext' */
    var fillcolor: String
    var showlegend: Boolean
    var legendgroup: String
    var parents: Array<String>
    var name: String
    var stackgroup: String
    var connectgaps: Boolean
    var visible: dynamic /* Boolean | 'legendonly' */
        get() = definedExternally
        set(value) = definedExternally
    var delta: DeltaPartial
    var gauge: GaugePartial
    var number: PlotNumberPartial
    var transforms: Array<DataTransform>
    var orientation: String /* 'v' | 'h' */
    var width: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var boxmean: dynamic /* Boolean | 'sd' */
        get() = definedExternally
        set(value) = definedExternally
    var opacity: Number
    var showscale: Boolean
    var colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */
        get() = definedExternally
        set(value) = definedExternally
    var zsmooth: dynamic /* 'fast' | 'best' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var ygap: Number
    var xgap: Number
    var transpose: Boolean
    var autobinx: Boolean
    var xbins: `T$7`
    var value: Number
    var values: Array<dynamic /* String | Number | Date | Nothing? */>
    var labels: Array<dynamic /* String | Number | Date | Nothing? */>
    var direction: String /* 'clockwise' | 'counterclockwise' */
    var hole: Number
    var rotation: Number
    var theta: Array<dynamic /* String | Number | Date | Nothing? */>
    var r: Array<dynamic /* String | Number | Date | Nothing? */>
    var customdata: Array<dynamic /* String | Number | Date | Nothing? */>
    var domain: `T$8`
    var title: DataTitlePartial
}

external interface PlotDataPartial {
    var type: String /* 'bar' | 'box' | 'candlestick' | 'choropleth' | 'contour' | 'heatmap' | 'histogram' | 'indicator' | 'mesh3d' | 'ohlc' | 'parcoords' | 'pie' | 'pointcloud' | 'scatter' | 'scatter3d' | 'scattergeo' | 'scattergl' | 'scatterpolar' | 'scatterternary' | 'surface' | 'treemap' | 'waterfall' | 'funnel' | 'funnelarea' */
    var x: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var y: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var z: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Array<Array<Array<dynamic /* String | Number | Date | Nothing? */>>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var xy: Float32Array?
        get() = definedExternally
        set(value) = definedExternally
    var error_x: ErrorOptionsPartial /* ErrorOptionsPartial & dynamic */
    var error_y: ErrorOptionsPartial /* ErrorOptionsPartial & dynamic */
    var xaxis: String?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis: String?
        get() = definedExternally
        set(value) = definedExternally
    var text: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var line: Any?
        get() = definedExternally
        set(value) = definedExternally
    var line.color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var line.width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var line.dash: String /* 'solid' | 'dot' | 'dash' | 'longdash' | 'dashdot' | 'longdashdot' */
    var line.shape: String /* 'linear' | 'spline' | 'hv' | 'vh' | 'hvh' | 'vhv' */
    var line.smoothing: Number?
        get() = definedExternally
        set(value) = definedExternally
    var line.simplify: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var marker: Any?
        get() = definedExternally
        set(value) = definedExternally
    var marker.symbol: dynamic /* String | Number | Array<dynamic /* String | Number */> | Array<dynamic /* String | Number | Array<dynamic /* String | Number */> */> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> | Array<dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.opacity: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.size: dynamic /* Number | Array<Number> | Array<Array<Number>> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.maxdisplayed: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.sizeref: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.sizemax: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.sizemin: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.sizemode: String /* 'diameter' | 'area' */
    var marker.showscale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var marker.line: Any?
        get() = definedExternally
        set(value) = definedExternally
    var marker.line.color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.line.colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> | Array<dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */> */
        get() = definedExternally
        set(value) = definedExternally
    var marker.colorbar: Any?
        get() = definedExternally
        set(value) = definedExternally
    var marker.pad.t: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.pad.b: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.pad.l: Number?
        get() = definedExternally
        set(value) = definedExternally
    var marker.pad.r: Number?
        get() = definedExternally
        set(value) = definedExternally
    var mode: String /* 'lines' | 'markers' | 'text' | 'lines+markers' | 'text+markers' | 'text+lines' | 'text+lines+markers' | 'none' | 'gauge' | 'number' | 'delta' | 'number+delta' | 'gauge+number' | 'gauge+number+delta' | 'gauge+delta' */
    var hoveron: String /* 'points' | 'fills' */
    var hoverinfo: String /* 'all' | 'name' | 'none' | 'skip' | 'text' | 'x' | 'x+text' | 'x+name' | 'x+y' | 'x+y+text' | 'x+y+name' | 'x+y+z' | 'x+y+z+text' | 'x+y+z+name' | 'y+name' | 'y+x' | 'y+text' | 'y+x+text' | 'y+x+name' | 'y+z' | 'y+z+text' | 'y+z+name' | 'y+x+z' | 'y+x+z+text' | 'y+x+z+name' | 'z+x' | 'z+x+text' | 'z+x+name' | 'z+y+x' | 'z+y+x+text' | 'z+y+x+name' | 'z+x+y' | 'z+x+y+text' | 'z+x+y+name' */
    var hoverlabel: Any?
        get() = definedExternally
        set(value) = definedExternally
    var hovertemplate: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var hovertext: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var textinfo: String /* 'label' | 'label+text' | 'label+value' | 'label+percent' | 'label+text+value' | 'label+text+percent' | 'label+value+percent' | 'text' | 'text+value' | 'text+percent' | 'text+value+percent' | 'value' | 'value+percent' | 'percent' | 'none' */
    var textposition: String /* "top left" | "top center" | "top right" | "middle left" | "middle center" | "middle right" | "bottom left" | "bottom center" | "bottom right" | "inside" | "outside" */
    var textfont: Any?
        get() = definedExternally
        set(value) = definedExternally
    var fill: String /* 'none' | 'tozeroy' | 'tozerox' | 'tonexty' | 'tonextx' | 'toself' | 'tonext' */
    var fillcolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var showlegend: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var legendgroup: String?
        get() = definedExternally
        set(value) = definedExternally
    var parents: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var stackgroup: String?
        get() = definedExternally
        set(value) = definedExternally
    var connectgaps: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var visible: dynamic /* Boolean | 'legendonly' */
        get() = definedExternally
        set(value) = definedExternally
    var delta: Any?
        get() = definedExternally
        set(value) = definedExternally
    var gauge: Any?
        get() = definedExternally
        set(value) = definedExternally
    var number: Any?
        get() = definedExternally
        set(value) = definedExternally
    var transforms: Array<DataTransform>?
        get() = definedExternally
        set(value) = definedExternally
    var orientation: String /* 'v' | 'h' */
    var width: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var boxmean: dynamic /* Boolean | 'sd' */
        get() = definedExternally
        set(value) = definedExternally
    var opacity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var showscale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */
        get() = definedExternally
        set(value) = definedExternally
    var zsmooth: dynamic /* 'fast' | 'best' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var ygap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var xgap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var transpose: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autobinx: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var xbins: `T$7`?
        get() = definedExternally
        set(value) = definedExternally
    var value: Number?
        get() = definedExternally
        set(value) = definedExternally
    var values: Array<dynamic /* String | Number | Date | Nothing? */>?
        get() = definedExternally
        set(value) = definedExternally
    var labels: Array<dynamic /* String | Number | Date | Nothing? */>?
        get() = definedExternally
        set(value) = definedExternally
    var direction: String /* 'clockwise' | 'counterclockwise' */
    var hole: Number?
        get() = definedExternally
        set(value) = definedExternally
    var rotation: Number?
        get() = definedExternally
        set(value) = definedExternally
    var theta: Array<dynamic /* String | Number | Date | Nothing? */>?
        get() = definedExternally
        set(value) = definedExternally
    var r: Array<dynamic /* String | Number | Date | Nothing? */>?
        get() = definedExternally
        set(value) = definedExternally
    var customdata: Array<dynamic /* String | Number | Date | Nothing? */>?
        get() = definedExternally
        set(value) = definedExternally
    var domain: `T$8`?
        get() = definedExternally
        set(value) = definedExternally
    var title: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TransformStyle {
    var target: dynamic /* Number | String | Array<Number> | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var value: PlotDataPartial
}

external interface TransformAggregation {
    var target: String
    var func: String /* 'count' | 'sum' | 'avg' | 'median' | 'mode' | 'rms' | 'stddev' | 'min' | 'max' | 'first' | 'last' */
    var funcmode: String /* 'sample' | 'population' */
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Transform {
    var type: String /* 'aggregate' | 'filter' | 'groupby' | 'sort' */
    var enabled: Boolean
    var target: dynamic /* Number | String | Array<Number> | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var operation: String
    var aggregations: Array<TransformAggregation>
    var preservegaps: Boolean
    var groups: dynamic /* String | Array<Number> | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var nameformat: String
    var styles: Array<TransformStyle>
    var value: Any
    var order: String /* 'ascending' | 'descending' */
}

external interface TransformPartial {
    var type: String /* 'aggregate' | 'filter' | 'groupby' | 'sort' */
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* Number | String | Array<Number> | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var operation: String?
        get() = definedExternally
        set(value) = definedExternally
    var aggregations: Array<TransformAggregation>?
        get() = definedExternally
        set(value) = definedExternally
    var preservegaps: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var groups: dynamic /* String | Array<Number> | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var nameformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var styles: Array<TransformStyle>?
        get() = definedExternally
        set(value) = definedExternally
    var value: Any?
        get() = definedExternally
        set(value) = definedExternally
    var order: String /* 'ascending' | 'descending' */
}

external interface `T$9` {
    var dtickrange: Array<Any>
    var value: String
}

external interface ColorBar {
    var thicknessmode: String /* 'fraction' | 'pixels' */
    var thickness: Number
    var lenmode: String /* 'fraction' | 'pixels' */
    var len: Number
    var x: Number
    var xanchor: String /* 'left' | 'center' | 'right' */
    var xpad: Number
    var y: Number
    var yanchor: String /* 'top' | 'middle' | 'bottom' */
    var ypad: Number
    var outlinecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var outlinewidth: Number
    var bordercolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var tickmode: String /* 'auto' | 'linear' | 'array' */
    var nticks: Number
    var tick0: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var dtick: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var tickvals: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Array<Array<Array<dynamic /* String | Number | Date | Nothing? */>>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var ticktext: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Array<Array<Array<dynamic /* String | Number | Date | Nothing? */>>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var ticks: String /* 'outside' | 'inside' | '' */
    var ticklen: Number
    var tickwidth: Number
    var tickcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var showticklabels: Boolean
    var tickfont: Font
    var tickangle: Number
    var tickformat: String
    var tickformatstops: `T$9`
    var tickprefix: String
    var showtickprefix: String /* 'all' | 'first' | 'last' | 'none' */
    var ticksuffix: String
    var showticksuffix: String /* 'all' | 'first' | 'last' | 'none' */
    var separatethousands: Boolean
    var exponentformat: String /* 'none' | 'e' | 'E' | 'power' | 'SI' | 'B' */
    var showexponent: String /* 'all' | 'first' | 'last' | 'none' */
    var title: String
    var titlefont: Font
    var titleside: String /* 'right' | 'top' | 'bottom' */
    var tickvalssrc: Any
    var ticktextsrc: Any
}

external interface ColorBarPartial {
    var thicknessmode: String /* 'fraction' | 'pixels' */
    var thickness: Number?
        get() = definedExternally
        set(value) = definedExternally
    var lenmode: String /* 'fraction' | 'pixels' */
    var len: Number?
        get() = definedExternally
        set(value) = definedExternally
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'left' | 'center' | 'right' */
    var xpad: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var yanchor: String /* 'top' | 'middle' | 'bottom' */
    var ypad: Number?
        get() = definedExternally
        set(value) = definedExternally
    var outlinecolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var outlinewidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var tickmode: String /* 'auto' | 'linear' | 'array' */
    var nticks: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tick0: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var dtick: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var tickvals: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Array<Array<Array<dynamic /* String | Number | Date | Nothing? */>>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var ticktext: dynamic /* Array<dynamic /* String | Number | Date | Nothing? */> | Array<Array<dynamic /* String | Number | Date | Nothing? */>> | Array<Array<Array<dynamic /* String | Number | Date | Nothing? */>>> | Int8Array | Uint8Array | Int16Array | Uint16Array | Int32Array | Uint32Array | Uint8ClampedArray | Float32Array | Float64Array */
        get() = definedExternally
        set(value) = definedExternally
    var ticks: String /* 'outside' | 'inside' | '' */
    var ticklen: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var showticklabels: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var tickfont: Font?
        get() = definedExternally
        set(value) = definedExternally
    var tickangle: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var tickformatstops: `T$9`?
        get() = definedExternally
        set(value) = definedExternally
    var tickprefix: String?
        get() = definedExternally
        set(value) = definedExternally
    var showtickprefix: String /* 'all' | 'first' | 'last' | 'none' */
    var ticksuffix: String?
        get() = definedExternally
        set(value) = definedExternally
    var showticksuffix: String /* 'all' | 'first' | 'last' | 'none' */
    var separatethousands: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var exponentformat: String /* 'none' | 'e' | 'E' | 'power' | 'SI' | 'B' */
    var showexponent: String /* 'all' | 'first' | 'last' | 'none' */
    var title: String?
        get() = definedExternally
        set(value) = definedExternally
    var titlefont: Font?
        get() = definedExternally
        set(value) = definedExternally
    var titleside: String /* 'right' | 'top' | 'bottom' */
    var tickvalssrc: Any?
        get() = definedExternally
        set(value) = definedExternally
    var ticktextsrc: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$10` {
    var type: String /* 'radial' | 'horizontal' | 'vertical' | 'none' */
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var typesrc: Any
    var colorsrc: Any
}

external interface PlotMarker {
    var symbol: dynamic /* String | Number | Array<dynamic /* String | Number */> */
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> | Array<dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */> */
        get() = definedExternally
        set(value) = definedExternally
    var colors: Array<dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */>
    var colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */
        get() = definedExternally
        set(value) = definedExternally
    var cauto: Boolean
    var cmax: Number
    var cmin: Number
    var autocolorscale: Boolean
    var reversescale: Boolean
    var opacity: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var size: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var maxdisplayed: Number
    var sizeref: Number
    var sizemax: Number
    var sizemin: Number
    var sizemode: String /* 'diameter' | 'area' */
    var showscale: Boolean
    var line: ScatterMarkerLinePartial
    var pad: PaddingPartial
    var width: Number
    var colorbar: ColorBarPartial
    var gradient: `T$10`
}

external interface PlotMarkerPartial {
    var symbol: dynamic /* String | Number | Array<dynamic /* String | Number */> */
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> | Array<dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */> */
        get() = definedExternally
        set(value) = definedExternally
    var colors: Array<dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */>?
        get() = definedExternally
        set(value) = definedExternally
    var colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */
        get() = definedExternally
        set(value) = definedExternally
    var cauto: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var cmax: Number?
        get() = definedExternally
        set(value) = definedExternally
    var cmin: Number?
        get() = definedExternally
        set(value) = definedExternally
    var autocolorscale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var reversescale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var opacity: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var size: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var maxdisplayed: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sizeref: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sizemax: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sizemin: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sizemode: String /* 'diameter' | 'area' */
    var showscale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var line: Any?
        get() = definedExternally
        set(value) = definedExternally
    var pad: Any?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var colorbar: Any?
        get() = definedExternally
        set(value) = definedExternally
    var gradient: `T$10`?
        get() = definedExternally
        set(value) = definedExternally
}

typealias ScatterMarker = PlotMarker

external interface ScatterMarkerLine {
    var width: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */
        get() = definedExternally
        set(value) = definedExternally
    var cauto: Boolean
    var cmax: Number
    var cmin: Number
    var autocolorscale: Boolean
    var reversescale: Boolean
}

external interface ScatterMarkerLinePartial {
    var width: dynamic /* Number | Array<Number> */
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var colorscale: dynamic /* String | Array<String> | Array<dynamic /* JsTuple<Number, String> */> */
        get() = definedExternally
        set(value) = definedExternally
    var cauto: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var cmax: Number?
        get() = definedExternally
        set(value) = definedExternally
    var cmin: Number?
        get() = definedExternally
        set(value) = definedExternally
    var autocolorscale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var reversescale: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ScatterLine {
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var width: Number
    var dash: String /* 'solid' | 'dot' | 'dash' | 'longdash' | 'dashdot' | 'longdashdot' */
    var shape: String /* 'linear' | 'spline' | 'hv' | 'vh' | 'hvh' | 'vhv' */
    var smoothing: Number
    var simplify: Boolean
}

external interface ScatterLinePartial {
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var dash: String /* 'solid' | 'dot' | 'dash' | 'longdash' | 'dashdot' | 'longdashdot' */
    var shape: String /* 'linear' | 'spline' | 'hv' | 'vh' | 'hvh' | 'vhv' */
    var smoothing: Number?
        get() = definedExternally
        set(value) = definedExternally
    var simplify: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Font {
    var family: String
    var size: Number
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FontPartial {
    var family: String?
        get() = definedExternally
        set(value) = definedExternally
    var size: Number?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Edits {
    var annotationPosition: Boolean
    var annotationTail: Boolean
    var annotationText: Boolean
    var axisTitleText: Boolean
    var colorbarPosition: Boolean
    var colorbarTitleText: Boolean
    var legendPosition: Boolean
    var legendText: Boolean
    var shapePosition: Boolean
    var titleText: Boolean
}

external interface EditsPartial {
    var annotationPosition: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var annotationTail: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var annotationText: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var axisTitleText: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var colorbarPosition: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var colorbarTitleText: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var legendPosition: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var legendText: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var shapePosition: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var titleText: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$11` {
    var filename: String?
        get() = definedExternally
        set(value) = definedExternally
    var scale: Number?
        get() = definedExternally
        set(value) = definedExternally
    var format: String /* 'png' | 'svg' | 'jpeg' | 'webp' */
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Config {
    var toImageButtonOptions: `T$11`
    var staticPlot: Boolean
    var editable: Boolean
    var edits: EditsPartial
    var autosizable: Boolean
    var queueLength: Number
    var fillFrame: Boolean
    var frameMargins: Number
    var scrollZoom: Boolean
    var doubleClick: dynamic /* 'reset+autosize' | 'reset' | 'autosize' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var showTips: Boolean
    var showAxisDragHandles: Boolean
    var showAxisRangeEntryBoxes: Boolean
    var showLink: Boolean
    var sendData: Boolean
    var linkText: String
    var showSources: Boolean
    var displayModeBar: dynamic /* 'hover' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var modeBarButtonsToRemove: Array<String /* 'lasso2d' | 'select2d' | 'sendDataToCloud' | 'zoom2d' | 'pan2d' | 'zoomIn2d' | 'zoomOut2d' | 'autoScale2d' | 'resetScale2d' | 'hoverClosestCartesian' | 'hoverCompareCartesian' | 'zoom3d' | 'pan3d' | 'orbitRotation' | 'tableRotation' | 'resetCameraDefault3d' | 'resetCameraLastSave3d' | 'hoverClosest3d' | 'zoomInGeo' | 'zoomOutGeo' | 'resetGeo' | 'hoverClosestGeo' | 'hoverClosestGl2d' | 'hoverClosestPie' | 'toggleHover' | 'toImage' | 'resetViews' | 'toggleSpikelines' */>
    var modeBarButtonsToAdd: dynamic /* Array<String /* 'lasso2d' | 'select2d' | 'sendDataToCloud' | 'zoom2d' | 'pan2d' | 'zoomIn2d' | 'zoomOut2d' | 'autoScale2d' | 'resetScale2d' | 'hoverClosestCartesian' | 'hoverCompareCartesian' | 'zoom3d' | 'pan3d' | 'orbitRotation' | 'tableRotation' | 'resetCameraDefault3d' | 'resetCameraLastSave3d' | 'hoverClosest3d' | 'zoomInGeo' | 'zoomOutGeo' | 'resetGeo' | 'hoverClosestGeo' | 'hoverClosestGl2d' | 'hoverClosestPie' | 'toggleHover' | 'toImage' | 'resetViews' | 'toggleSpikelines' */> | Array<ModeBarButton> */
        get() = definedExternally
        set(value) = definedExternally
    var modeBarButtons: dynamic /* Array<Array<String /* 'lasso2d' | 'select2d' | 'sendDataToCloud' | 'zoom2d' | 'pan2d' | 'zoomIn2d' | 'zoomOut2d' | 'autoScale2d' | 'resetScale2d' | 'hoverClosestCartesian' | 'hoverCompareCartesian' | 'zoom3d' | 'pan3d' | 'orbitRotation' | 'tableRotation' | 'resetCameraDefault3d' | 'resetCameraLastSave3d' | 'hoverClosest3d' | 'zoomInGeo' | 'zoomOutGeo' | 'resetGeo' | 'hoverClosestGeo' | 'hoverClosestGl2d' | 'hoverClosestPie' | 'toggleHover' | 'toImage' | 'resetViews' | 'toggleSpikelines' */>> | Array<Array<ModeBarButton>> | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var displaylogo: Boolean
    var plotGlPixelRatio: Number
    var setBackground: () -> String
    var topojsonURL: String
    var mapboxAccessToken: String
    var logging: dynamic /* Boolean | 0 | 1 | 2 */
        get() = definedExternally
        set(value) = definedExternally
    var globalTransforms: Array<Any>
    var locale: String
    var responsive: Boolean
}

external interface ConfigPartial {
    var toImageButtonOptions: `T$11`?
        get() = definedExternally
        set(value) = definedExternally
    var staticPlot: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var editable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var edits: Any?
        get() = definedExternally
        set(value) = definedExternally
    var autosizable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var queueLength: Number?
        get() = definedExternally
        set(value) = definedExternally
    var fillFrame: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var frameMargins: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scrollZoom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var doubleClick: dynamic /* 'reset+autosize' | 'reset' | 'autosize' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var showTips: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showAxisDragHandles: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showAxisRangeEntryBoxes: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showLink: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var sendData: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var linkText: String?
        get() = definedExternally
        set(value) = definedExternally
    var showSources: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var displayModeBar: dynamic /* 'hover' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var modeBarButtonsToRemove: Array<String /* 'lasso2d' | 'select2d' | 'sendDataToCloud' | 'zoom2d' | 'pan2d' | 'zoomIn2d' | 'zoomOut2d' | 'autoScale2d' | 'resetScale2d' | 'hoverClosestCartesian' | 'hoverCompareCartesian' | 'zoom3d' | 'pan3d' | 'orbitRotation' | 'tableRotation' | 'resetCameraDefault3d' | 'resetCameraLastSave3d' | 'hoverClosest3d' | 'zoomInGeo' | 'zoomOutGeo' | 'resetGeo' | 'hoverClosestGeo' | 'hoverClosestGl2d' | 'hoverClosestPie' | 'toggleHover' | 'toImage' | 'resetViews' | 'toggleSpikelines' */>?
        get() = definedExternally
        set(value) = definedExternally
    var modeBarButtonsToAdd: dynamic /* Array<String /* 'lasso2d' | 'select2d' | 'sendDataToCloud' | 'zoom2d' | 'pan2d' | 'zoomIn2d' | 'zoomOut2d' | 'autoScale2d' | 'resetScale2d' | 'hoverClosestCartesian' | 'hoverCompareCartesian' | 'zoom3d' | 'pan3d' | 'orbitRotation' | 'tableRotation' | 'resetCameraDefault3d' | 'resetCameraLastSave3d' | 'hoverClosest3d' | 'zoomInGeo' | 'zoomOutGeo' | 'resetGeo' | 'hoverClosestGeo' | 'hoverClosestGl2d' | 'hoverClosestPie' | 'toggleHover' | 'toImage' | 'resetViews' | 'toggleSpikelines' */> | Array<ModeBarButton> */
        get() = definedExternally
        set(value) = definedExternally
    var modeBarButtons: dynamic /* Array<Array<String /* 'lasso2d' | 'select2d' | 'sendDataToCloud' | 'zoom2d' | 'pan2d' | 'zoomIn2d' | 'zoomOut2d' | 'autoScale2d' | 'resetScale2d' | 'hoverClosestCartesian' | 'hoverCompareCartesian' | 'zoom3d' | 'pan3d' | 'orbitRotation' | 'tableRotation' | 'resetCameraDefault3d' | 'resetCameraLastSave3d' | 'hoverClosest3d' | 'zoomInGeo' | 'zoomOutGeo' | 'resetGeo' | 'hoverClosestGeo' | 'hoverClosestGl2d' | 'hoverClosestPie' | 'toggleHover' | 'toImage' | 'resetViews' | 'toggleSpikelines' */>> | Array<Array<ModeBarButton>> | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var displaylogo: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var plotGlPixelRatio: Number?
        get() = definedExternally
        set(value) = definedExternally
    var setBackground: (() -> String)?
        get() = definedExternally
        set(value) = definedExternally
    var topojsonURL: String?
        get() = definedExternally
        set(value) = definedExternally
    var mapboxAccessToken: String?
        get() = definedExternally
        set(value) = definedExternally
    var logging: dynamic /* Boolean | 0 | 1 | 2 */
        get() = definedExternally
        set(value) = definedExternally
    var globalTransforms: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var locale: String?
        get() = definedExternally
        set(value) = definedExternally
    var responsive: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RangeSlider {
    var visible: Boolean
    var thickness: Number
    var range: dynamic /* JsTuple<dynamic, dynamic> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number
    var bordercolor: String
    var bgcolor: String
}

external interface RangeSliderPartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var thickness: Number?
        get() = definedExternally
        set(value) = definedExternally
    var range: dynamic /* JsTuple<dynamic, dynamic> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RangeSelectorButton {
    var step: String /* 'second' | 'minute' | 'hour' | 'day' | 'month' | 'year' | 'all' */
    var stepmode: String /* 'backward' | 'todate' */
    var count: Number
    var label: String
}

external interface RangeSelectorButtonPartial {
    var step: String /* 'second' | 'minute' | 'hour' | 'day' | 'month' | 'year' | 'all' */
    var stepmode: String /* 'backward' | 'todate' */
    var count: Number?
        get() = definedExternally
        set(value) = definedExternally
    var label: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RangeSelector : Label {
    var buttons: Array<RangeSelectorButtonPartial>
    var visible: Boolean
    var x: Number
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var y: Number
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var activecolor: String
    var borderwidth: Number
}

external interface RangeSelectorPartial : LabelPartial {
    var buttons: Array<Partial<RangeSelectorButton>>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var activecolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Camera {
    var up: PointPartial
    var center: PointPartial
    var eye: PointPartial
}

external interface CameraPartial {
    var up: Any?
        get() = definedExternally
        set(value) = definedExternally
    var center: Any?
        get() = definedExternally
        set(value) = definedExternally
    var eye: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Label {
    var bgcolor: String
    var bordercolor: String
    var font: FontPartial
}

external interface LabelPartial {
    var bgcolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var font: FontPartial?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HoverLabel : Label {
    var align: String /* "left" | "right" | "auto" */
    var namelength: Number
}

external interface HoverLabelPartial : LabelPartial {
    var align: String /* "left" | "right" | "auto" */
    var namelength: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Annotations : Label {
    var visible: Boolean
    var text: String
    var textangle: String
    var width: Number
    var height: Number
    var opacity: Number
    var align: String /* 'left' | 'center' | 'right' */
    var valign: String /* 'top' | 'middle' | 'bottom' */
    var borderpad: Number
    var borderwidth: Number
    var showarrow: Boolean
    var arrowcolor: String
    var arrowhead: Number
    var startarrowhead: Number
    var arrowside: String /* 'end' | 'start' */
    var arrowsize: Number
    var startarrowsize: Number
    var arrowwidth: Number
    var standoff: Number
    var startstandoff: Number
    var ax: Number
    var ay: Number
    var axref: String /* 'pixel' */
    var ayref: String /* 'pixel' */
    var xref: String /* 'paper' | 'x' */
    var x: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var xshift: Number
    var yref: String /* 'paper' | 'y' */
    var y: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var yshift: Number
    var clicktoshow: dynamic /* Boolean | 'onoff' | 'onout' */
        get() = definedExternally
        set(value) = definedExternally
    var xclick: Any
    var yclick: Any
    var hovertext: String
    var hoverlabel: HoverLabelPartial
    var captureevents: Boolean
}

external interface AnnotationsPartial : LabelPartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var text: String?
        get() = definedExternally
        set(value) = definedExternally
    var textangle: String?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally
    var opacity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var align: String /* 'left' | 'center' | 'right' */
    var valign: String /* 'top' | 'middle' | 'bottom' */
    var borderpad: Number?
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var showarrow: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var arrowcolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var arrowhead: Number?
        get() = definedExternally
        set(value) = definedExternally
    var startarrowhead: Number?
        get() = definedExternally
        set(value) = definedExternally
    var arrowside: String /* 'end' | 'start' */
    var arrowsize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var startarrowsize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var arrowwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var standoff: Number?
        get() = definedExternally
        set(value) = definedExternally
    var startstandoff: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ax: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var axref: String /* 'pixel' */
    var ayref: String /* 'pixel' */
    var xref: String /* 'paper' | 'x' */
    var x: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var xshift: Number?
        get() = definedExternally
        set(value) = definedExternally
    var yref: String /* 'paper' | 'y' */
    var y: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var yshift: Number?
        get() = definedExternally
        set(value) = definedExternally
    var clicktoshow: dynamic /* Boolean | 'onoff' | 'onout' */
        get() = definedExternally
        set(value) = definedExternally
    var xclick: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yclick: Any?
        get() = definedExternally
        set(value) = definedExternally
    var hovertext: String?
        get() = definedExternally
        set(value) = definedExternally
    var hoverlabel: Any?
        get() = definedExternally
        set(value) = definedExternally
    var captureevents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Image {
    var visible: Boolean
    var source: String
    var layer: String /* 'above' | 'below' */
    var sizex: Number
    var sizey: Number
    var sizing: String /* 'fill' | 'contain' | 'stretch' */
    var opacity: Number
    var x: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var y: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'left' | 'center' | 'right' */
    var yanchor: String /* 'top' | 'middle' | 'bottom' */
    var xref: String /* 'paper' | 'x' */
    var yref: String /* 'paper' | 'y' */
}

external interface ImagePartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var source: String?
        get() = definedExternally
        set(value) = definedExternally
    var layer: String /* 'above' | 'below' */
    var sizex: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sizey: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sizing: String /* 'fill' | 'contain' | 'stretch' */
    var opacity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var x: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var y: dynamic /* Number | String */
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'left' | 'center' | 'right' */
    var yanchor: String /* 'top' | 'middle' | 'bottom' */
    var xref: String /* 'paper' | 'x' */
    var yref: String /* 'paper' | 'y' */
}

external interface Scene {
    var bgcolor: String
    var camera: CameraPartial
    var domain: DomainPartial
    var aspectmode: String /* 'auto' | 'cube' | 'data' | 'manual' */
    var aspectratio: PointPartial
    var xaxis: SceneAxisPartial
    var yaxis: SceneAxisPartial
    var zaxis: SceneAxisPartial
    var dragmode: dynamic /* 'orbit' | 'turntable' | 'zoom' | 'pan' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var hovermode: dynamic /* 'closest' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var annotations: dynamic /* AnnotationsPartial | Array<AnnotationsPartial> */
        get() = definedExternally
        set(value) = definedExternally
    var captureevents: Boolean
}

external interface ScenePartial {
    var bgcolor: String?
        get() = definedExternally
        set(value) = definedExternally
    var camera: Any?
        get() = definedExternally
        set(value) = definedExternally
    var domain: Any?
        get() = definedExternally
        set(value) = definedExternally
    var aspectmode: String /* 'auto' | 'cube' | 'data' | 'manual' */
    var aspectratio: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var yaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var zaxis: Any?
        get() = definedExternally
        set(value) = definedExternally
    var dragmode: dynamic /* 'orbit' | 'turntable' | 'zoom' | 'pan' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var hovermode: dynamic /* 'closest' | Boolean */
        get() = definedExternally
        set(value) = definedExternally
    var annotations: dynamic /* Partial<Annotations> | Array<Partial<Annotations>> */
        get() = definedExternally
        set(value) = definedExternally
    var captureevents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Domain {
    var x: Array<Number>
    var y: Array<Number>
    var row: Number
    var column: Number
}

external interface DomainPartial {
    var x: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var y: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var row: Number?
        get() = definedExternally
        set(value) = definedExternally
    var column: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Frame {
    var group: String
    var name: String
    var traces: Array<Number>
    var baseframe: String
    var data: Array<Data>
    var layout: LayoutPartial
}

external interface FramePartial {
    var group: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var traces: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var baseframe: String?
        get() = definedExternally
        set(value) = definedExternally
    var data: Array<Data>?
        get() = definedExternally
        set(value) = definedExternally
    var layout: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Transition {
    var duration: Number
    var easing: String /* 'linear' | 'quad' | 'cubic' | 'sin' | 'exp' | 'circle' | 'elastic' | 'back' | 'bounce' | 'linear-in' | 'quad-in' | 'cubic-in' | 'sin-in' | 'exp-in' | 'circle-in' | 'elastic-in' | 'back-in' | 'bounce-in' | 'linear-out' | 'quad-out' | 'cubic-out' | 'sin-out' | 'exp-out' | 'circle-out' | 'elastic-out' | 'back-out' | 'bounce-out' | 'linear-in-out' | 'quad-in-out' | 'cubic-in-out' | 'sin-in-out' | 'exp-in-out' | 'circle-in-out' | 'elastic-in-out' | 'back-in-out' | 'bounce-in-out' */
    var ordering: String /* 'layout first' | 'traces first' */
}

external interface SliderStep {
    var visible: Boolean
    var method: String /* 'animate' | 'relayout' | 'restyle' | 'skip' | 'update' */
    var args: Array<Any>
    var label: String
    var value: String
    var execute: Boolean
}

external interface SliderStepPartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var method: String /* 'animate' | 'relayout' | 'restyle' | 'skip' | 'update' */
    var args: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var label: String?
        get() = definedExternally
        set(value) = definedExternally
    var value: String?
        get() = definedExternally
        set(value) = definedExternally
    var execute: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Padding {
    var t: Number
    var r: Number
    var b: Number
    var l: Number
    var editType: String /* 'arraydraw' */
}

external interface PaddingPartial {
    var t: Number?
        get() = definedExternally
        set(value) = definedExternally
    var r: Number?
        get() = definedExternally
        set(value) = definedExternally
    var b: Number?
        get() = definedExternally
        set(value) = definedExternally
    var l: Number?
        get() = definedExternally
        set(value) = definedExternally
    var editType: String /* 'arraydraw' */
}

external interface `T$12` {
    var visible: Boolean
    var xanchor: String /* 'left' | 'center' | 'right' */
    var offset: Number
    var prefix: String
    var suffix: String
    var font: FontPartial
}

external interface Slider {
    var visible: Boolean
    var active: Number
    var steps: Array<SliderStepPartial>
    var lenmode: String /* 'fraction' | 'pixels' */
    var len: Number
    var x: Number
    var y: Number
    var pad: PaddingPartial
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var transition: Transition
    var currentvalue: `T$12`
    var font: Font
    var activebgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number
    var ticklen: Number
    var tickcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var tickwidth: Number
    var minorticklen: Number
}

external interface SliderPartial {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var active: Number?
        get() = definedExternally
        set(value) = definedExternally
    var steps: Array<Partial<SliderStep>>?
        get() = definedExternally
        set(value) = definedExternally
    var lenmode: String /* 'fraction' | 'pixels' */
    var len: Number?
        get() = definedExternally
        set(value) = definedExternally
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pad: Any?
        get() = definedExternally
        set(value) = definedExternally
    var xanchor: String /* 'auto' | 'left' | 'center' | 'right' */
    var yanchor: String /* 'auto' | 'top' | 'middle' | 'bottom' */
    var transition: Transition?
        get() = definedExternally
        set(value) = definedExternally
    var currentvalue: `T$12`?
        get() = definedExternally
        set(value) = definedExternally
    var font: Font?
        get() = definedExternally
        set(value) = definedExternally
    var activebgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bgcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var bordercolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var borderwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ticklen: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tickcolor: dynamic /* String | Number | Array<dynamic /* String | Number | Nothing? | Nothing? */> | Array<Array<dynamic /* String | Number | Nothing? | Nothing? */>> */
        get() = definedExternally
        set(value) = definedExternally
    var tickwidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var minorticklen: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external fun restyle(root: String, aobj: Data): Promise<PlotlyHTMLElement>

external fun restyle(root: HTMLElement, aobj: Data): Promise<PlotlyHTMLElement>

external fun update(root: String, traceUpdate: Data, layoutUpdate: LayoutPartial): Promise<PlotlyHTMLElement>

external fun update(root: HTMLElement, traceUpdate: Data, layoutUpdate: LayoutPartial): Promise<PlotlyHTMLElement>

external fun addTraces(root: String, traces: Data): Promise<PlotlyHTMLElement>

external fun addTraces(root: String, traces: Array<Data>): Promise<PlotlyHTMLElement>

external fun addTraces(root: HTMLElement, traces: Data): Promise<PlotlyHTMLElement>

external fun addTraces(root: HTMLElement, traces: Array<Data>): Promise<PlotlyHTMLElement>

external fun moveTraces(root: String, currentIndices: Array<Number>): Promise<PlotlyHTMLElement>

external fun moveTraces(root: String, currentIndices: Number): Promise<PlotlyHTMLElement>

external fun moveTraces(root: HTMLElement, currentIndices: Array<Number>): Promise<PlotlyHTMLElement>

external fun moveTraces(root: HTMLElement, currentIndices: Number): Promise<PlotlyHTMLElement>