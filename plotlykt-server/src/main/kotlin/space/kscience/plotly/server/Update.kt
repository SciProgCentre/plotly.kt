package space.kscience.plotly.server

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.toJson


private val coordinateNames = listOf(
    "x", "y", "z", "text", "close", "high", "low", "open"
)

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
            //patch json to adhere to plotly array in array specification
            val contentJson: JsonObject = content.toJson() as? JsonObject
                ?: buildJsonObject { put("@value", content.toJson()) }
            val patchedJson = contentJson + coordinateNames.associateWith { contentJson[it] }
                .filter { it.value != null }
                .mapValues { JsonArray(listOf(it.value!!)) }
            put("content", JsonObject(patchedJson))
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