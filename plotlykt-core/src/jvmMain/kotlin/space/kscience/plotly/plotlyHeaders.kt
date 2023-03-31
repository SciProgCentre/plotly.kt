package space.kscience.plotly

import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.unsafe
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption


internal const val PLOTLY_SCRIPT_PATH = "/js/plotly.min.js"
//const val PLOTLY_PROMISE_NAME = "promiseOfPlotly"
/**
 * Check if the asset exists in given local location and put it there if it does not
 */
internal fun checkOrStoreFile(basePath: Path, filePath: Path, resource: String): Path {
    val fullPath = basePath.resolveSibling(filePath).toAbsolutePath()

    if (Files.exists(fullPath)) {
        //TODO checksum
    } else {
        //TODO add logging

        val bytes = PlotlyHtmlFragment::class.java.getResourceAsStream(resource)!!.readAllBytes()
        Files.createDirectories(fullPath.parent)
        Files.write(fullPath, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
    }

    return if (basePath.isAbsolute && fullPath.startsWith(basePath)) {
        basePath.relativize(fullPath)
    } else {
        filePath
    }
}

/**
 * A header that automatically copies relevant scripts to given path
 */
public fun localScriptHeader(
    basePath: Path,
    scriptPath: Path,
    resource: String,
): PlotlyHtmlFragment = PlotlyHtmlFragment {
    val relativePath = checkOrStoreFile(basePath, scriptPath, resource)
    script {
        type = "text/javascript"
        src = relativePath.toString()
        attributes["onload"] = "console.log('Script successfully loaded from $relativePath')"
        attributes["onerror"] = "console.log('Failed to load script from $relativePath')"
    }
}

public fun localCssHeader(
    basePath: Path,
    cssPath: Path,
    resource: String,
): PlotlyHtmlFragment = PlotlyHtmlFragment {
    val relativePath = checkOrStoreFile(basePath, cssPath, resource)
    link {
        rel = "stylesheet"
        href = relativePath.toString()
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
