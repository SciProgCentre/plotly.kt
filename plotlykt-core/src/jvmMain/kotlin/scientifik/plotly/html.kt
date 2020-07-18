package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

interface HtmlVisitor {
    fun visit(consumer: TagConsumer<*>)
}

/**
 * Create a html (including headers) string from plot
 */
fun Plot.toHTML(vararg headers: HtmlVisitor, config: PlotlyConfig = PlotlyConfig()): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            title(layout.title ?: "Plotly.kt")
            //Apply cdn header by default
            if (headers.isEmpty()) {
                PlotlyCdnHeader.visit(consumer)
            } else {
                headers.forEach {
                    it.visit(consumer)
                }
            }
        }
        body {
            StaticPlotlyContainer.run {
                renderPlot(this@toHTML, "plot", config)
            }
        }
    }
}

/**
 * Check if the asset exists in given local location and put it there if it does not
 */
internal fun checkOrStoreFile(basePath: Path, filePath: Path, resource: String): Path {
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
) : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>): Unit {
        val relativePath = checkOrStoreFile(basePath, scriptPath, resource)
        consumer.script {
            type = "text/javascript"
            src = relativePath.toString()
            attributes["onload"] = "console.log('Script successfully loaded from $relativePath')"
            attributes["onerror"] = "console.log('Failed to load script from $relativePath')"
        }
    }
}

class LocalCssHeader(
    val basePath: Path,
    val cssPath: Path,
    val resource: String
) : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>): Unit {
        val relativePath = checkOrStoreFile(basePath, cssPath, resource)
        consumer.link {
            rel = "stylesheet"
            href = relativePath.toString()
        }
    }
}

