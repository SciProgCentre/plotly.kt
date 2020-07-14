package scientifik.plotly.models

import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.boolean
import hep.dataforge.meta.enum
import hep.dataforge.meta.numberList
import scientifik.plotly.doubleInRange

enum class PieDirection {
    clockwise,
    counterclockwise
}

class Pie() : Trace() {
    init {
        type = TraceType.pie
    }

    /**
     * Sets the fraction of larger radius to pull the sectors out from the center.
     * This can be a constant to pull all slices apart from each other
     * equally or an array to highlight one or more slices.
     * Default: 0.
     */
    var pull by numberList()

    /**
     * Specifies the direction at which succeeding sectors follow one another.
     */
    var direction by enum(PieDirection.counterclockwise)

    /**
     * Sets the fraction of the radius to cut out of the pie.
     * Use this to make a donut chart. Default: 0.
     */
    var hole by doubleInRange(0.0..1.0)

    /**
     * Instead of the first slice starting at 12 o'clock, rotate to some other angle.
     * Default: 0.
     */
    var rotation by doubleInRange(-360.0..360.0)

    /**
     * Determines whether or not the sectors are reordered from largest to smallest.
     * Default: true.
     */
    var sort by boolean()

    companion object : SchemeSpec<Pie>(::Pie)
}