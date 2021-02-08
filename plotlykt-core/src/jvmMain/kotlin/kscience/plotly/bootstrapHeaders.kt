package kscience.plotly

import kotlinx.html.link
import kotlinx.html.script
import java.nio.file.Path


private const val bootstrapJsPath = "/js/bootstrap.bundle.min.js"
private const val jQueryPath = "/js/jquery-3.5.1.slim.min.js"
private const val bootstrapCssPath = "/css/bootstrap.min.css"

public fun localBootstrap(basePath: Path) = HtmlFragment {
    script {
        type = "text/javascript"
        src = checkOrStoreFile(
            basePath,
            Path.of(assetsDirectory + jQueryPath),
            jQueryPath
        ).toString()
    }
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

public val cdnBootstrap: HtmlFragment = HtmlFragment {
    script {
        src = "https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity = "sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        attributes["crossorigin"] =  "anonymous"
    }
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

