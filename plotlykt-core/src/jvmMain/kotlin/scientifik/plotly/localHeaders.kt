package scientifik.plotly

import kotlinx.html.HEAD
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.unsafe
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

private const val assetsDirectory = "plotly-assets"

private const val plotlyResource = "/js/plotly.min.js"

private val localStorePath by lazy {
    Path.of(System.getProperty("user.home")).resolve(".plotly/${assetsDirectory}")
}

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
    } else {
        filePath
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
    override fun invoke(head: HEAD): Unit {
        val relativePath = checkOrStoreFile(basePath, scriptPath, resource)
        head.script {
            src = relativePath.toString()
        }
    }
}

class LocalCssHeader(
    val basePath: Path,
    val cssPath: Path,
    val resource: String
) : HtmlHeader {
    override fun invoke(head: HEAD): Unit {
        val relativePath = checkOrStoreFile(basePath, cssPath, resource)
        head.link {
            rel = "stylesheet"
            href = relativePath.toString()
        }
    }
}

fun LocalPlotlyJs(path: Path, relativeScriptPath: String = "$assetsDirectory/plotly.min.js"): HtmlHeader {
    return LocalScriptHeader(path, Path.of(relativeScriptPath), plotlyResource)
}

/**
 * A system-wide plotly store location
 */
val SystemPlotlyJs = LocalScriptHeader(
    Path.of("."),
    localStorePath.resolve(plotlyResource.removePrefix("/")),
    plotlyResource
)


/**
 * embedded plotly script
 */
object EmbededPlotlyJs : HtmlHeader {
    override fun invoke(head: HEAD) {
        head.script {
            unsafe {
                val bytes = LocalScriptHeader::class.java.getResourceAsStream(plotlyResource).readAllBytes()
                +bytes.toString(Charsets.UTF_8)
            }
        }
    }
}

private const val bootstrapJsPath = "/js/bootstrap.bundle.min.js"
private const val bootstrapCssPath = "/css/bootstrap.min.css"

class LocalBootstrap(val basePath: Path) : HtmlHeader {
    override fun invoke(head: HEAD): Unit = head.run {
        script {
            src = checkOrStoreFile(
                basePath,
                Path.of(assetsDirectory).resolve(bootstrapJsPath.removePrefix("/")),
                bootstrapJsPath
            ).toString()
        }
        link {
            rel = "stylesheet"
            href = checkOrStoreFile(
                basePath,
                Path.of(assetsDirectory).resolve(bootstrapCssPath.removePrefix("/")),
                bootstrapCssPath
            ).toString()
        }
    }
}