package space.kscience.plotly.models

import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.dataforge.meta.scheme
import space.kscience.plotly.numberGreaterThan
import space.kscience.plotly.numberInRange

public class SelectMarker : Scheme() {
    /**
     * Sets the marker opacity of selected points.
     */
    public var opacity: Number by numberInRange(0.0..1.0)

    /**
     * Sets the marker color of selected points.
     */
    public val color: Color by color()

    /**
     * Sets the marker size of selected points.
     */
    public var size: Number by numberGreaterThan(0)

    public companion object : SchemeSpec<SelectMarker>(::SelectMarker)
}

public class SelectPoints : Scheme() {
    public var marker: SelectMarker by scheme(SelectMarker)

    public var textfont: Font by scheme(Font)

    public fun marker(block: SelectMarker.() -> Unit) {
        marker = SelectMarker(block)
    }

    public fun textfont(block: Font.() -> Unit) {
        textfont = Font(block)
    }

    public companion object : SchemeSpec<SelectPoints>(::SelectPoints)
}

public interface SelectedPoints {
    /**
     * Array containing integer indices of selected points. Has an effect only for traces that support selections.
     * Note that an empty array means an empty selection where the `unselected` are turned on for all points, whereas,
     * any other non-array values means no selection all where the `selected` and `unselected` styles have no effect.
     */
    public var selectedpoints: List<Number>

    public var selected: SelectPoints

    public var unselected: SelectPoints
}