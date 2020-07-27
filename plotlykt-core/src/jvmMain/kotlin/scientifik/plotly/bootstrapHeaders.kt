package scientifik.plotly

import kotlinx.html.link
import kotlinx.html.script
import java.nio.file.Path


private const val bootstrapJsPath = "/js/bootstrap.bundle.min.js"
private const val bootstrapCssPath = "/css/bootstrap.min.css"

fun localBootstrap(basePath: Path) = HtmlFragment {
    script {
        type = "text/javascript"
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

val cdnBootstrap = HtmlFragment {
    script {
        src = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"
        integrity = "sha384-1CmrxMRARb6aLqgBO7yyAxTOQE2AKb9GfXnEo760AUcUmFx3ibVJJAzGytlQcNXd"
        attributes["crossorigin"] = "anonymous"
    }
    link {
        rel = "stylesheet"
        href = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
        attributes["integrity"] = "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
        attributes["crossorigin"] = "anonymous"
    }
}

