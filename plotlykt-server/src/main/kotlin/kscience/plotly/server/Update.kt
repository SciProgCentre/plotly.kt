package kscience.plotly.server

import hep.dataforge.meta.Meta
import hep.dataforge.meta.toJson
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json

/**
 * An update message for both data and layout
 */
sealed class Update(val id: String) {
    abstract fun toJson(): JsonObject

    class Trace(id: String, val trace: Int, val content: Meta) : Update(id) {
        override fun toJson(): JsonObject = json {
            "plotId" to id
            "contentType" to "trace"
            "trace" to trace
            "content" to content.toJson()
        }

    }

    class Layout(id: String, val content: Meta) : Update(id) {
        override fun toJson(): JsonObject = json {
            "plotId" to id
            "contentType" to "layout"
            "content" to content.toJson()
        }
    }
}