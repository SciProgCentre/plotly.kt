package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.*
import space.kscience.plotly.models.Color
import space.kscience.plotly.models.Layout
import space.kscience.plotly.models.color
import kotlin.js.JsName

public enum class GeoScope{
    africa,
    asia,
    europe,
    @JsName("northAmerica")
    `north america`,
    @JsName("southAmerica")
    `south america`,
    usa,
    world
}


public class Geo : Scheme() {
    public val bgcolor: Color by color()
    public var center: MapCoordinates by scheme(MapCoordinates)

    public val coastlinecolor: Color by color()
    public var coastlinewidth: Number by number(1)

    public val countrycolor: Color by color()
    public var countrywidth: Number by number(1)

    public var fitbounds: Value? by value()

    public val framecolor: Color by color()
    public var framewidth: Number by number(1)

    public val lakecolor: Color by color()

    public val landcolor: Color by color()

    public val oceancolor: Color by color()

    public val rivercolor: Color by color()
    public var riverwidth: Number by number(1)

    public val subunitcolor: Color by color()
    public var subunitwidth: Number by number(1)


    public var showcoastlines: Boolean? by boolean()
    public var showcountries: Boolean? by boolean()
    public var showframe: Boolean? by boolean()
    public var showlakes: Boolean? by boolean()
    public var showland: Boolean? by boolean()
    public var showocean: Boolean? by boolean()

    public var lataxis: MapAxis by scheme(MapAxis)
    public var lonaxis: MapAxis by scheme(MapAxis)

    public var projection: GeoProjection by scheme(GeoProjection)

    public var scope: GeoScope by enum(GeoScope.world)

    public companion object : SchemeSpec<Geo>(::Geo)
}

public var Layout.geo: Geo
    get() = Geo.write(meta.getOrCreate("geo"))
    set(value){
        meta["geo"] = value.meta
    }