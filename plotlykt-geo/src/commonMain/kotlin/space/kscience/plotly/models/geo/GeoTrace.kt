package space.kscience.plotly.models.geo

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.asName
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.TraceValues
import space.kscience.plotly.models.geo.json.GeoJsonFeatureCollection

public abstract class GeoTrace : Trace() {

    public val locations: TraceValues by axis

    /**
     *
     * Sets optional GeoJSON data associated with this trace. If not given, the features on the base map are used.
     * It can be set as a valid GeoJSON object or as a URL string. Note that we only accept GeoJSONs of type
     * "FeatureCollection" or "Feature" with geometries of type "Polygon" or "MultiPolygon".
     * TODO replace by typed wrapper
     */
    public var geojson: Meta? by node()

    /**
     * Set GeoJson from in-memory Json
     */
    public fun geoJsonFeatures(collections: GeoJsonFeatureCollection) {
        geojson = collections.json.toMeta()
    }

    /**
     * An url to geojson
     */
    public var geojsonUrl: String? by string(key = "geojson".asName())

    /**
     * Sets the key in GeoJSON features which is used as id to match the items included in the `locations` array.
     * Only has an effect when `geojson` is set. Support nested property, for example "properties.name".
     */
    public var featureidkey: String by string("id")

    /**
     * Determines whether the colorscale is a default palette (`autocolorscale: true`) or the palette determined
     * by `colorscale`. In case `colorscale` is unspecified or `autocolorscale` is true, the default palette will
     * be chosen according to whether numbers in the `color` array are all positive, all negative or mixed.
     */
    public var autocolorscale: Boolean by boolean(true)

    /**
     * Sets a reference between this trace's geospatial coordinates and a geographic map. If "geo" (the default value),
     * the geospatial coordinates refer to `layout.geo`. If "geo2", the geospatial coordinates refer to `layout.geo2`,
     * and so on.
     */
    public var geo: String? by string()

    public var locationmode: LocationMode by enum(LocationMode.`ISO-3`)

}