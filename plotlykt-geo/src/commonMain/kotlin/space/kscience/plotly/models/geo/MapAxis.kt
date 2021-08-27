package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.values.ListValue
import space.kscience.dataforge.values.Value
import space.kscience.dataforge.values.asValue
import space.kscience.plotly.models.Color
import space.kscience.plotly.models.color


public class MapAxis : Scheme() {
    /**
     *  Sets the graticule's longitude/latitude tick step.
     */
    public var dtick: Number? by number()

    /**
     *  Sets the graticule's stroke color.
     */
    public val gridcolor: Color by color()

    /**
     *  Sets the graticule's stroke width (in px).
     */
    public var gridwidth: Number by number(1)

    /**
     *  Sets the range of this axis (in degrees), sets the map's clipped coordinates.
     */
    public fun range(from: Value, to: Value) {
        meta["range"] = ListValue(listOf(from, to))
    }

    public fun range(value: ClosedFloatingPointRange<Double>) {
        range(value.start.asValue(), value.endInclusive.asValue())
    }

    /**
     *  Sets whether graticule are shown on the map.
     */
    public var showgrid: Boolean? by boolean()

    /**
     *    Sets the graticule's starting tick longitude/latitude.
     */
    public var tick0: Number by number(0)

    public companion object : SchemeSpec<MapAxis>(::MapAxis)
}