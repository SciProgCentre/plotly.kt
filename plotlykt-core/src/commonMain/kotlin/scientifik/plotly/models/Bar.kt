package scientifik.plotly.models

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.numberList
import hep.dataforge.meta.spec

class Bar() : Scheme(), SelectedPoints {
    /**
     * Array containing integer indices of selected points. Has an effect only for traces that support selections.
     * Note that an empty array means an empty selection where the `unselected` are turned on for all points, whereas,
     * any other non-array values means no selection all where the `selected` and `unselected` styles have no effect.
     */
    override var selectedpoints by numberList()

    override var selected by spec(SelectPoints)

    override var unselected by spec(SelectPoints)

    companion object : SchemeSpec<Bar>(::Bar)
}