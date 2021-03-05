package space.kscience.plotly

import kotlinx.html.script
import kotlinx.html.unsafe
import space.kscience.plotly.Plotly.PLOTLY_CDN
import java.nio.file.Path


internal const val PLOTLY_SCRIPT_PATH = "/js/plotly.min.js"
//const val PLOTLY_PROMISE_NAME = "promiseOfPlotly"

public val cdnPlotlyHeader: PlotlyHtmlFragment = PlotlyHtmlFragment {
    script {
        type = "text/javascript"
        src = PLOTLY_CDN
    }
}


internal fun localPlotlyHeader(
    path: Path,
    relativeScriptPath: String = "$assetsDirectory$PLOTLY_SCRIPT_PATH"
) = PlotlyHtmlFragment {
    val relativePath = checkOrStoreFile(path, Path.of(relativeScriptPath), PLOTLY_SCRIPT_PATH)
    script {
        type = "text/javascript"
        src = relativePath.toString()
    }
}


/**
 * A system-wide plotly store location
 */
internal val systemPlotlyHeader = PlotlyHtmlFragment {
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
internal val embededPlotlyHeader = PlotlyHtmlFragment {
    script {
        unsafe {
            val bytes = PlotlyHtmlFragment::class.java.getResourceAsStream(PLOTLY_SCRIPT_PATH)!!.readAllBytes()
            +bytes.toString(Charsets.UTF_8)
        }
    }
}


internal fun inferPlotlyHeader(
    target: Path?,
    resourceLocation: ResourceLocation
): PlotlyHtmlFragment = when (resourceLocation) {
    ResourceLocation.REMOTE -> cdnPlotlyHeader
    ResourceLocation.LOCAL -> if (target != null) {
        localPlotlyHeader(target)
    } else {
        systemPlotlyHeader
    }
    ResourceLocation.SYSTEM -> systemPlotlyHeader
    ResourceLocation.EMBED -> embededPlotlyHeader
}
