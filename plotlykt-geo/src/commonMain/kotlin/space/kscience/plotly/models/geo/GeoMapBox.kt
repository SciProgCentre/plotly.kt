package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.*
import space.kscience.plotly.Plot
import space.kscience.plotly.models.Layout

public class GeoMapBox : Scheme() {
    /**
     * Sets the mapbox access token to be used for this mapbox map. Alternatively, the mapbox access token can be set
     * in the configuration options under `mapboxAccessToken`. Note that accessToken are only required when `style`
     * (e.g with values : basic, streets, outdoors, light, dark, satellite, satellite-streets ) and/or a layout layer
     * references the Mapbox server.
     */
    public var accesstoken: String? by string()

    /**
     * Sets the bearing angle of the map in degrees counter-clockwise from North (mapbox.bearing).
     */
    public var bearing: Number by number(0)

    /**
     * Sets the pitch angle of the map (in degrees, where "0" means perpendicular to the surface of the map) (mapbox.pitch).
     */
    public var pitch: Number by number(0)

    public var center: MapCoordinates by spec(MapCoordinates)

    //TODO domain

    //TODO layers

    /**
     * Defines the map layers that are rendered by default below the trace layers defined in `data`, which are
     * themselves by default rendered below the layers defined in `layout.mapbox.layers`. These layers can be defined
     * either explicitly as a Mapbox Style object which can contain multiple layer definitions that load data from any
     * public or private Tile Map Service (TMS or XYZ) or Web Map Service (WMS) or implicitly by using one of the
     * built-in style objects which use WMSes which do not require any access tokens, or by using a default Mapbox
     * style or custom Mapbox style URL, both of which require a Mapbox access token Note that Mapbox access token can
     * be set in the `accesstoken` attribute or in the `mapboxAccessToken` config option. Mapbox Style objects are of
     * the form described in the Mapbox GL JS documentation available at https://docs.mapbox.com/mapbox-gl-js/style-spec
     * The built-in plotly.js styles objects are: carto-darkmatter, carto-positron, open-street-map, stamen-terrain,
     * stamen-toner, stamen-watercolor, white-bg The built-in Mapbox styles are: basic, streets, outdoors, light, dark,
     * satellite, satellite-streets Mapbox style URLs are of the form: mapbox://mapbox.mapbox-<name>-<version>
     */
    public var style: Value? by value()

    /**
     * Sets the zoom level of the map (mapbox.zoom).
     */
    public var zoom: Number by number(1.0)

    public companion object : SchemeSpec<GeoMapBox>(::GeoMapBox)
}

public fun GeoMapBox.useOpenStreetMap() {
    style = "open-street-map".asValue()
}

public var Layout.mapbox: GeoMapBox
    get() = GeoMapBox.write(meta.getOrCreate("mapbox"))
    set(value) {
        meta["mapbox"] = value.meta
    }

public fun Plot.openStreetMap(block: GeoMapBox.() -> Unit) {
    layout.mapbox {
        useOpenStreetMap()
        block()
    }
}