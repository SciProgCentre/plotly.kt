package space.kscience.plotly.models

import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.dataforge.meta.enum
import space.kscience.dataforge.meta.numberList
import space.kscience.dataforge.names.asName
import space.kscience.plotly.numberGreaterThan
import space.kscience.plotly.numberInRange

public enum class LineShape {
    hv,
    vh,
    hvh,
    vhv,
    spline,
    linear
}

public enum class Dash {
    solid,
    dot,
    dash,
    longdash,
    dashdot,
    longdashdot
}

public class LayoutLine : Scheme(), Line {
    /**
     * Sets the line color.
     */
    override val color: Color by color()

    /**
     * Sets the line width (in px). Default: 2.
     */
    override var width: Number by numberGreaterThan(0)

    /**
     * Sets the width (in px) of the lines bounding the marker points.
     */
    override var widthList: List<Number> by numberList(key = "width".asName())

    /**
     * Determines the line shape. With "spline" the lines
     * are drawn using spline interpolation. The other
     * available values correspond to step-wise line shapes.
     * Default: "linear"
     */
    public var shape: LineShape by enum(LineShape.linear)

    /**
     * Sets the style of the lines.
     */
    public var dash: Dash by enum(Dash.solid)

    /**
     * Sets the amount of smoothing for the contour lines, where "0" corresponds to no smoothing.
     * Default: 1.
     */
    public var smoothing: Number by numberInRange(0.0..1.3)

    public companion object : SchemeSpec<LayoutLine>(::LayoutLine)
}