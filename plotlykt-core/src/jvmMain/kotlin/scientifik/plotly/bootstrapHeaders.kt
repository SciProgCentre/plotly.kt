package scientifik.plotly

import kotlinx.html.link
import kotlinx.html.script
import java.nio.file.Path


private const val bootstrapJsPath = "/js/bootstrap.bundle.min.js"
private const val bootstrapCssPath = "/css/bootstrap.min.css"

fun localBootstrap(basePath: Path) = html {
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

