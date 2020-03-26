package scientifik.plotly.server

import hep.dataforge.meta.Meta
import hep.dataforge.meta.toJson
import kotlinx.serialization.json.JsonObject

/**
 * An update message for both data and layout
 */
sealed class Update(val page: String, val plot: String) {
    abstract fun toJson(): JsonObject

    class Trace(page: String, plot: String, val trace: Int, val content: Meta) : Update(page, plot) {
        override fun toJson(): JsonObject = Meta {
            "page" to page
            "plot" to plot
            "contentType" to "trace"
            "trace" to trace
            "content" to content
        }.toJson()

    }

    class Layout(page: String, plot: String, val content: Meta) : Update(page, plot) {
        override fun toJson(): JsonObject = Meta {
            "page" to page
            "plot" to plot
            "contentType" to "layout"
            "content" to content
        }.toJson()
    }
}