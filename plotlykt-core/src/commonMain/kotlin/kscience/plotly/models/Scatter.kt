package kscience.plotly.models

import hep.dataforge.meta.*
import kotlin.js.JsName

public enum class ScatterMode {
    lines,
    markers,
    text,
    none,

    @JsName("linesMarkers")
    `lines+markers`,

    @JsName("linesText")
    `lines+text`,

    @JsName("markersText")
    `markers+text`,

    @JsName("linesMarkersText")
    `lines+markers+text`
}

public enum class GroupNorm {
    @JsName("empty")
    ` `,
    fraction,
    percent
}

public enum class FillType {
    none,
    tozeroy,
    tozerox,
    tonexty,
    tonextx,
    toself,
    tonext
}

public enum class ScatterHoveron {
    points,
    fills,

    @JsName("pointsAndFills")
    `points+fills`
}

public enum class StackGaps {
    @JsName("inferZero")
    `infer zero`,
    interpolate
}

public open class Scatter : Trace(), SelectedPoints {
    init {
        type = TraceType.scatter
    }

    /**
     * Flaglist string. Any combination of "lines", "markers", "text"
     * joined with a "+" OR "none". Determines the drawing mode for
     * this scatter trace. If the provided `mode` includes "text" then
     * the `text` elements appear at the coordinates. Otherwise,
     * the `text` elements appear on hover.
     * Default: lines.
     */
    public var mode: ScatterMode by enum(ScatterMode.lines)

    /**
     * Only relevant when `stackgroup` is used, and only the first `groupnorm` found in the `stackgroup` will be used -
     * including if `visible` is "legendonly" but not if it is `false`. Sets the normalization for the sum of
     * this `stackgroup`. With "fraction", the value of each trace at each location is divided by the sum of all
     * trace values at that location. "percent" is the same but multiplied by 100 to show percentages. If there are
     * multiple subplots, or multiple `stackgroup`s on one subplot, each will be normalized within its own set.
     */
    public var groupnorm: GroupNorm by enum(GroupNorm.` `)

    /**
     * Set several scatter traces (on the same subplot) to the same stackgroup in order to add their
     * y values (or their x values if `orientation` is "h"). If blank or omitted this trace will not be stacked.
     * Stacking also turns `fill` on by default, using "tonexty" ("tonextx") if `orientation` is "h" ("v")
     * and sets the default `mode` to "lines" irrespective of point count. You can only stack on a numeric
     * (linear or log) axis. Traces in a `stackgroup` will only fill to (or be filled to) other traces in the
     * same group. With multiple `stackgroup`s or some traces stacked and some not, if fill-linked traces are
     * not already consecutive, the later ones will be pushed down in the drawing order.
     * Default: "".
     */
    public var stackgroup: String? by string()

    /**
     * Array containing integer indices of selected points. Has an effect only for traces that support selections.
     * Note that an empty array means an empty selection where the `unselected` are turned on for all points, whereas,
     * any other non-array values means no selection all where the `selected` and `unselected` styles have no effect.
     */
    override var selectedpoints: List<Number> by numberList()

    override var selected: SelectPoints? by spec(SelectPoints)

    override var unselected: SelectPoints? by spec(SelectPoints)

    /**
     * Sets the area to fill with a solid color. Defaults to "none" unless this trace is stacked, then
     * it gets "tonexty" ("tonextx") if `orientation` is "v" ("h") Use with `fillcolor` if not "none".
     * "tozerox" and "tozeroy" fill to x=0 and y=0 respectively. "tonextx" and "tonexty" fill between
     * the endpoints of this trace and the endpoints of the trace before it, connecting those endpoints
     * with straight lines (to make a stacked area graph); if there is no trace before it, they behave
     * like "tozerox" and "tozeroy". "toself" connects the endpoints of the trace (or each segment
     * of the trace if it has gaps) into a closed shape. "tonext" fills the space between two traces
     * if one completely encloses the other (eg consecutive contour lines), and behaves like "toself"
     * if there is no trace before it. "tonext" should not be used if one trace does not enclose the other.
     * Traces in a `stackgroup` will only fill to (or be filled to) other traces in the same group.
     * With multiple `stackgroup`s or some traces stacked and some not, if fill-linked traces
     * are not already consecutive, the later ones will be pushed down in the drawing order.
     */
    public var fill: FillType by enum(FillType.none)

    /**
     * Do the hover effects highlight individual points (markers or line points) or do they
     * highlight filled regions? If the fill is "toself" or "tonext" and there are no markers or text,
     * then the default is "fills", otherwise it is "points".
     */
    public var hoveron: ScatterHoveron by enum(ScatterHoveron.points)

    /**
     * Only relevant when `stackgroup` is used, and only the first `stackgaps` found in the `stackgroup`
     * will be used - including if `visible` is "legendonly" but not if it is `false`. Determines how
     * we handle locations at which other traces in this group have data but this one does not.
     * With "infer zero" we insert a zero at these locations. With "interpolate" we linearly interpolate
     * between existing values, and extrapolate a constant beyond the existing values.
     */
    public var stackgaps: StackGaps by enum(StackGaps.`infer zero`)

    public fun selected(block: SelectPoints.() -> Unit) {
        selected = SelectPoints(block)
    }

    public fun unselected(block: SelectPoints.() -> Unit) {
        unselected = SelectPoints(block)
    }

    public companion object : SchemeSpec<Scatter>(::Scatter)
}

public class ScatterGL : Scatter() {
    init {
        type = TraceType.scattergl
    }

    public companion object : SchemeSpec<ScatterGL>(::ScatterGL)
}