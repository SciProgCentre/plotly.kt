package scientifik.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.spec
import hep.dataforge.names.asName
import scientifik.plotly.numberGreaterThan
import scientifik.plotly.numberInRange

class SelectMarker : Scheme() {
    /**
     * Sets the marker opacity of selected points.
     */
    var opacity by numberInRange(0.0..1.0)

    /**
     * Sets the marker color of selected points.
     */
    var color = Color(this, "color".asName())

    /**
     * Sets the marker size of selected points.
     */
    var size by numberGreaterThan(0)

    companion object : SchemeSpec<SelectMarker>(::SelectMarker)
}

class SelectPoints : Scheme() {
    var marker by spec(SelectMarker)

    var textfont by spec(Font)

    fun marker(block: SelectMarker.() -> Unit) {
        marker = SelectMarker(block)
    }

    fun textfont(block: Font.() -> Unit) {
        textfont = Font(block)
    }

    companion object : SchemeSpec<SelectPoints>(::SelectPoints)
}

interface SelectedPoints {
    /**
     * Array containing integer indices of selected points. Has an effect only for traces that support selections.
     * Note that an empty array means an empty selection where the `unselected` are turned on for all points, whereas,
     * any other non-array values means no selection all where the `selected` and `unselected` styles have no effect.
     */
    var selectedpoints: List<Number>

    var selected: SelectPoints?

    var unselected: SelectPoints?
}