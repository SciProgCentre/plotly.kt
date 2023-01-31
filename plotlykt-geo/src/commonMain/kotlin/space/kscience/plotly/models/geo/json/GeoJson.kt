package space.kscience.plotly.models.geo.json

import kotlinx.serialization.json.*

/**
 * An utility class to work with GeoJson (https://geojson.org/)
 */
public open class GeoJson(public val json: JsonObject) {
    public val type: String get() = json["type"]?.jsonPrimitive?.content ?: error("Not a GeoJson")
}

public class GeoJsonFeature(json: JsonObject) : GeoJson(json) {
    init {
        require(type == "Feature") { "Not a GeoJson Feature" }
    }

    public val id: JsonPrimitive? get() = json["id"]?.jsonPrimitive

    public val properties: JsonObject? get() = json["properties"]?.jsonObject

    public fun getProperty(propertyName: String): JsonPrimitive? = properties?.get(propertyName)?.jsonPrimitive

    public fun getString(propertyName: String): String? = getProperty(propertyName)?.contentOrNull
}

public class GeoJsonFeatureCollection(json: JsonObject) : GeoJson(json), Iterable<GeoJsonFeature> {
    init {
        require(type == "FeatureCollection") { "Not a GeoJson FeatureCollection" }
    }

    public val features: List<GeoJsonFeature>
        get() = json["features"]!!.jsonArray.map {
            GeoJsonFeature(it.jsonObject)
        }

    override fun iterator(): Iterator<GeoJsonFeature> = features.iterator()

    public companion object {
        public fun parse(string: String): GeoJsonFeatureCollection = GeoJsonFeatureCollection(
            Json.parseToJsonElement(string).jsonObject
        )
    }
}

/**
 * Combine a collection of features to a new [GeoJsonFeatureCollection]
 */
public fun Collection<GeoJsonFeature>.combine(): GeoJsonFeatureCollection = GeoJsonFeatureCollection(
    buildJsonObject {
        put("type", "FeatureCollection")
        put("features", JsonArray(this@combine.map { it.json }))
    }
)