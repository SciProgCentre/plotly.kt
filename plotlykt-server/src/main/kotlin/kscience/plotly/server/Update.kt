package kscience.plotly.server

import hep.dataforge.meta.Meta
import hep.dataforge.meta.toJson
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * An update message for both data and layout
 */
public sealed class Update(public val id: String) {
    public abstract fun toJson(): JsonObject

    public class Trace(id: String, private val trace: Int, private val content: Meta) : Update(id) {
        override fun toJson(): JsonObject = buildJsonObject {
            put("plotId", id)
            put("contentType", "trace")
            put("trace", trace)
            put("content", content.toJson())
        }

    }

    public class Layout(id: String, private val content: Meta) : Update(id) {
        override fun toJson(): JsonObject = buildJsonObject {
            put("plotId", id)
            put("contentType", "layout")
            put("content", content.toJson())
        }
    }
}