package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.*
import space.kscience.plotly.numberGreaterThan

public class GeoProjection : Scheme() {
    /**
     * For satellite projection type only. Sets the distance from the center of the sphere to
     * the point of view as a proportion of the sphere’s radius.
     */
    public var distance: Number by numberGreaterThan(1.001, default = 2.0)
    public var rotation: MapCoordinatesWithRotation by spec(MapCoordinatesWithRotation)

    /**
     * Zooms in or out on the map view. A scale of "1" corresponds
     * to the largest zoom level that fits the map's lon and lat ranges.
     */
    public var scale: Number by number(1.0)

    /**
     * For satellite projection type only. Sets the tilt angle of perspective projection.
     */
    public var tilt: Number by number(0.0)

    public var type: String? by string()

    public companion object : SchemeSpec<GeoProjection>(::GeoProjection)
}