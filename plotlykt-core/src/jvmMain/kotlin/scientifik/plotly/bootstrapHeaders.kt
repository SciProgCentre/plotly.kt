package scientifik.plotly

import kotlinx.html.TagConsumer
import kotlinx.html.link
import kotlinx.html.script
import java.nio.file.Path


private const val bootstrapJsPath = "/js/bootstrap.bundle.min.js"
private const val bootstrapCssPath = "/css/bootstrap.min.css"

class LocalBootstrap(val basePath: Path) : HtmlVisitor {
    override fun visit(consumer: TagConsumer<*>): Unit = consumer.run {
        script {
            src = checkOrStoreFile(
                basePath,
                Path.of(assetsDirectory + bootstrapJsPath),
                bootstrapJsPath
            ).toString()
        }
        link {
            rel = "stylesheet"
            href = checkOrStoreFile(
                basePath,
                Path.of(assetsDirectory + bootstrapCssPath),
                bootstrapCssPath
            ).toString()
        }
    }
}
