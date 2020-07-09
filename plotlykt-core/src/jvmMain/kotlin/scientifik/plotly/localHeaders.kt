package scientifik.plotly

import kotlinx.html.META
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.unsafe
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption


private const val plotlyResource = "/js/plotly.min.js"

private fun checkOrStoreFile(basePath: Path, filePath: Path, resource: String): Path {
    val fullPath = basePath.resolveSibling(filePath).toAbsolutePath()

    if (Files.exists(fullPath)) {
        //TODO checksum
    } else {
        //TODO add logging

        val bytes = LocalScriptHeader::class.java.getResourceAsStream(resource).readAllBytes()
        Files.createDirectories(fullPath.parent)
        Files.write(fullPath, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
    }

    return if (basePath.isAbsolute && fullPath.startsWith(basePath)) {
        basePath.relativize(fullPath)
    } else if (filePath.isAbsolute) {
        filePath
    } else {
        fullPath
    }
}

/**
 * A header that automatically copies relevant scripts to given path
 */
class LocalScriptHeader(
    val basePath: Path,
    val scriptPath: Path,
    val resource: String
) : HtmlHeader {
    override fun invoke(meta: META): Unit {
        val relativePath = checkOrStoreFile(basePath, scriptPath, resource)
        meta.script {
            src = relativePath.toString()
        }
    }
}

class LocalCssHeader(
    val basePath: Path,
    val cssPath: Path,
    val resource: String
) : HtmlHeader {
    override fun invoke(meta: META): Unit {
        val relativePath = checkOrStoreFile(basePath, cssPath, resource)
        meta.link {
            rel = "stylesheet"
            href = relativePath.toString()
        }
    }
}

fun localPlotlyJs(path: Path, relativeScriptPath: String = "plotly-assets/plotly.min.js"): HtmlHeader {
    return LocalScriptHeader(path, Path.of(relativeScriptPath), plotlyResource)
}

/**
 * A system-wide plotly store location
 */
val SystemPlotlyJs = LocalScriptHeader(
    Path.of("."),
    Path.of(System.getProperty("user.home")).resolve(".plotly/plotly-assets/plotly.min.js"),
    plotlyResource
)


/**
 * embedded plotly script
 */
object EmbededPlotlyJs : HtmlHeader {
    override fun invoke(meta: META) {
        meta.script {
            unsafe {
                val bytes = LocalScriptHeader::class.java.getResourceAsStream(plotlyResource).readAllBytes()
                +bytes.toString(Charsets.UTF_8)
            }
        }
    }

}