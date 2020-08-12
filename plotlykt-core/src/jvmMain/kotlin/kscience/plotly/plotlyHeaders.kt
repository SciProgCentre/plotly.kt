package kscience.plotly

import kotlinx.html.script
import kotlinx.html.unsafe
import java.nio.file.Path


const val PLOTLY_SCRIPT_PATH = "/js/plotly.min.js"
const val PLOTLY_PROMISE_NAME = "promiseOfPlotly"

const val PLOTLY_CDN = "https://cdn.plot.ly/plotly-${Plotly.VERSION}.min.js"
//"https://cdnjs.cloudflare.com/ajax/libs/plotly.js/${Plotly.VERSION}/plotly.min.js"

val cdnPlotlyHeader = HtmlFragment {
    script {
        type = "text/javascript"
        src = PLOTLY_CDN
    }
}


fun localPlotlyHeader(
    path: Path,
    relativeScriptPath: String = "$assetsDirectory$PLOTLY_SCRIPT_PATH"
) = HtmlFragment {
    val relativePath = checkOrStoreFile(path, Path.of(relativeScriptPath), PLOTLY_SCRIPT_PATH)
    script {
        type = "text/javascript"
        src = relativePath.toString()
    }
}


/**
 * A system-wide plotly store location
 */
val systemPlotlyHeader = HtmlFragment {
    val relativePath = checkOrStoreFile(
        Path.of("."),
        Path.of(System.getProperty("user.home")).resolve(".plotly/$assetsDirectory$PLOTLY_SCRIPT_PATH"),
        PLOTLY_SCRIPT_PATH
    )
    script {
        type = "text/javascript"
        src = relativePath.toString()
    }
}


/**
 * embedded plotly script
 */
val embededPlotlyHeader = HtmlFragment {
    script {
        unsafe {
            val bytes = HtmlFragment::class.java.getResourceAsStream(PLOTLY_SCRIPT_PATH).readAllBytes()
            +bytes.toString(Charsets.UTF_8)
        }
    }
}


internal fun inferPlotlyHeader(
    target: Path?,
    resourceLocation: ResourceLocation
): HtmlFragment = when (resourceLocation) {
    ResourceLocation.REMOTE -> cdnPlotlyHeader
    ResourceLocation.LOCAL -> if (target != null) {
        localPlotlyHeader(target)
    } else {
        systemPlotlyHeader
    }
    ResourceLocation.SYSTEM -> systemPlotlyHeader
    ResourceLocation.EMBED -> embededPlotlyHeader
}
