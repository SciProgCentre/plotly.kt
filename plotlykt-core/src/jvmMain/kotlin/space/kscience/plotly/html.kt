package space.kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

public class PlotlyHtmlFragment(public val visit: TagConsumer<*>.() -> Unit) {
    override fun toString(): String {
        return createHTML().also(visit).finalize()
    }
}

public operator fun PlotlyHtmlFragment.plus(other: PlotlyHtmlFragment): PlotlyHtmlFragment = PlotlyHtmlFragment {
    this@plus.run { visit() }
    other.run { visit() }
}

/**
 * Create a html (including headers) string from plot
 */
public fun Plot.toHTML(
    vararg headers: PlotlyHtmlFragment = arrayOf(cdnPlotlyHeader),
    config: PlotlyConfig = PlotlyConfig(),
): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            title(layout.title ?: "Plotly.kt")
            headers.forEach {
                it.visit(consumer)
            }
        }
        body {
            StaticPlotlyRenderer.run {
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

public val mathJaxHeader: PlotlyHtmlFragment = PlotlyHtmlFragment {
    script {
        type = "text/x-mathjax-config"
        unsafe {
            +"""
            MathJax.Hub.Config({
                tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
            });
            """
        }
    }
    script {
        type = "text/javascript"
        async = true
        src = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-MML-AM_SVG"
    }
}