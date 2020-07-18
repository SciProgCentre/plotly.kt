package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class HtmlBuilder(val visit: TagConsumer<*>.() -> Unit){
    override fun toString(): String{
        return createHTML().also(visit).finalize()
    }
}



fun html(visit: TagConsumer<*>.() -> Unit) = HtmlBuilder(visit)

/**
 * Create a html (including headers) string from plot
 */
fun Plot.toHTML(vararg headers: HtmlBuilder, config: PlotlyConfig = PlotlyConfig()): String {
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

        val bytes = HtmlBuilder::class.java.getResourceAsStream(resource).readAllBytes()
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
fun localScriptHeader(
    basePath: Path,
    scriptPath: Path,
    resource: String
) = html {
    val relativePath = checkOrStoreFile(basePath, scriptPath, resource)
    script {
        type = "text/javascript"
        src = relativePath.toString()
        attributes["onload"] = "console.log('Script successfully loaded from $relativePath')"
        attributes["onerror"] = "console.log('Failed to load script from $relativePath')"
    }
}

fun localCssHeader(
    basePath: Path,
    cssPath: Path,
    resource: String
) = html {
    val relativePath = checkOrStoreFile(basePath, cssPath, resource)
    link {
        rel = "stylesheet"
        href = relativePath.toString()
    }
}
