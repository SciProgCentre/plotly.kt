package scientifik.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

interface HtmlHeader {
    operator fun invoke(head: HEAD)
}

object PlotlyCdnHeader : HtmlHeader {
    override fun invoke(head: HEAD) = head.script {
        this.src = "https://cdn.plot.ly/plotly-latest.min.js"
    }
}

fun HEAD.applyHeaders(headers: Array<out HtmlHeader>) {
    //Apply cdn header by default
    if (headers.isEmpty()) {
        PlotlyCdnHeader(this)
    } else {
        headers.forEach {
            it(this)
        }
    }
}

fun TagConsumer<*>.staticPlot(
    plot: Plot2D,
    plotId: String = plot.toString(),
    plotlyConfig: PlotlyConfig = PlotlyConfig()
): Plot2D {
    div {
        id = plotId
        script {
            defer = true
            val tracesString = plot.data.toJsonString()
            val layoutString = plot.layout.toJsonString()
            unsafe {
                //language=JavaScript
                +"""
                    
                    Plotly.react(
                        '$plotId',
                        $tracesString,
                        $layoutString,
                        $plotlyConfig
                    );
                    
                """.trimIndent()
            }
        }
    }
    return plot
}

fun FlowContent.staticPlot(
    plot: Plot2D,
    plotId: String = plot.toString(),
    plotlyConfig: PlotlyConfig = PlotlyConfig()
): Plot2D = consumer.staticPlot(plot, plotId, plotlyConfig)

//fun FlowContent.staticPlot(
//    plotId: String? = null,
//    plotlyConfig: PlotlyConfig = PlotlyConfig(),
//    builder: Plot2D.() -> Unit
//): Plot2D {
//    val plot = Plot2D().apply(builder)
//    return staticPlot(plot, plotId ?: plot.toString(), plotlyConfig)
//}

/**
 * Create a html (including headers) string from plot
 */
fun Plot2D.toHTML(vararg headers: HtmlHeader, config: PlotlyConfig = PlotlyConfig()): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            applyHeaders(headers)
            title(layout.title ?: "Plotly.kt")
        }
        body {
            staticPlot(this@toHTML, "plot", config)
        }
    }
}


private const val assetsDirectory = "plotly-assets"

private const val plotlyResource = "/js/plotly.min.js"

private val localStorePath by lazy {
    Path.of(System.getProperty("user.home")).resolve(".plotly/${assetsDirectory}")
}

/**
 * Check if the asset exists in given local location and put it there if it does not
 */
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

internal fun inferPlotlyHeader(target: Path?, resourceLocation: ResourceLocation): HtmlHeader =
    when (resourceLocation) {
        ResourceLocation.REMOTE -> PlotlyCdnHeader
        ResourceLocation.LOCAL -> if (target != null) {
            LocalPlotlyJs(target)
        } else {
            SystemPlotlyJs
        }
        ResourceLocation.SYSTEM -> SystemPlotlyJs
        ResourceLocation.EMBED -> EmbededPlotlyJs
    }


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

object PlotlyJupyter {
    const val JUPYTER_ASSETS_PATH = ".jupyter_kotlin/assets/"
    private const val PLOTLY_DEFINED_FLAG = "kotlinPlotly"

    fun initScript(): String {
        val filePath = checkOrStoreFile(
            Path.of("."),
            Path.of(System.getProperty("user.home")).resolve(JUPYTER_ASSETS_PATH + plotlyResource),
            plotlyResource
        )
        return createHTML().div {
            val divID = "${PLOTLY_DEFINED_FLAG}-init"
            id = divID
            script {
                unsafe {
                    //language=JavaScript
                    +"""

                    if(typeof window.$PLOTLY_DEFINED_FLAG === 'undefined'){
                        (function() {
                            let script = document.createElement("script");
                            script.type = "text/javascript";
                            script.src = "${filePath}";
                            script.onload = function() {
                                window.$PLOTLY_DEFINED_FLAG = true
                            };
                            script.onerror = function(event) {
                                let div = document.createElement("div");
                                div.style.color = 'darkred';
                                div.textContent = 'Error loading Plotly JS';
                                document.getElementById("$divID").appendChild(div);
                            };
                            var e = document.getElementById("$divID");
                            e.appendChild(script);
                        })();
    
                    }

                    """.trimIndent()
                }
            }
        }
    }

    fun plotToHTML(plot: Plot2D): String = createHTML().div {
        staticPlot(plot)
    }

    fun pageToHTML(page: PlotlyPage): String = createHTML().div {
        with(page) {
            renderPage(StaticPlotlyContainer(this@div))
        }
    }

}