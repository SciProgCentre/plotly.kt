package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.dataforge.meta.number

public open class MapCoordinates : Scheme() {
    public var lat: Number? by number()
    public var lon: Number? by number()

    public companion object : SchemeSpec<MapCoordinates>(::MapCoordinates)
}

public class MapCoordinatesWithRotation : MapCoordinates() {
    public var roll: Number? by number()
    public companion object : SchemeSpec<MapCoordinatesWithRotation>(::MapCoordinatesWithRotation)
}

