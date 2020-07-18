package scientifik.plotly

import kotlinx.html.TagConsumer
import kotlinx.html.script
import kotlinx.html.unsafe
import java.nio.file.Path


internal const val plotlyResource = "/js/plotly.min.js"

const val PLOTLY_PROMISE_NAME = "promiseOfPlotly"

internal const val PLOTLY_VERSION = "1.54.6"

object PlotlyCdnHeader : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>): Unit {
        consumer.script {
            attributes["onload"] = "window.$PLOTLY_PROMISE_NAME = Promise.resolve(Plotly)"
            attributes["onerror"] = "console.log('Failed to load Plotly from CDN')"
            type = "text/javascript"
            src = "https://cdnjs.cloudflare.com/ajax/libs/plotly.js/${PLOTLY_VERSION}/plotly.min.js"
        }
    }
}

class LocalPlotlyJs(
    val path: Path,
    val relativeScriptPath: String = "$assetsDirectory$plotlyResource"
) : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>): Unit {
        val relativePath = checkOrStoreFile(path, Path.of(relativeScriptPath), plotlyResource)
        consumer.script {
            attributes["onload"] = "window.$PLOTLY_PROMISE_NAME = Promise.resolve(Plotly)"
            attributes["onerror"] = "console.log('Failed to load script from $relativePath')"
            type = "text/javascript"
            src = relativePath.toString()
        }
    }
}

/**
 * A system-wide plotly store location
 */
object SystemPlotlyJs : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>) {
        val relativePath = checkOrStoreFile(
            Path.of("."),
            Path.of(System.getProperty("user.home")).resolve(".plotly/$assetsDirectory$plotlyResource"),
            plotlyResource
        )
        consumer.script {
            attributes["onload"] = "window.$PLOTLY_PROMISE_NAME = Promise.resolve(Plotly)"
            attributes["onerror"] = "console.log('Failed to load script from $relativePath')"
            type = "text/javascript"
            src = relativePath.toString()
        }
    }
}


/**
 * embedded plotly script
 */
object EmbededPlotlyJs : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>) {
        consumer.script {
            attributes["onload"] = "window.$PLOTLY_PROMISE_NAME = Promise.resolve(Plotly)"
            attributes["onerror"] = "console.log('Failed to load embed script')"
            unsafe {
                val bytes = LocalScriptHeader::class.java.getResourceAsStream(plotlyResource).readAllBytes()
                +bytes.toString(Charsets.UTF_8)
            }
        }
    }
}


internal fun inferPlotlyHeader(
    target: Path?,
    resourceLocation: ResourceLocation
): HtmlVisitor = when (resourceLocation) {
    ResourceLocation.REMOTE -> PlotlyCdnHeader
    ResourceLocation.LOCAL -> if (target != null) {
        LocalPlotlyJs(target)
    } else {
        SystemPlotlyJs
    }
    ResourceLocation.SYSTEM -> SystemPlotlyJs
    ResourceLocation.EMBED -> EmbededPlotlyJs
}
