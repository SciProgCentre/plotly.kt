package scientifik.plotly.assets

import scientifik.plotly.assets.AssetsProvidingMode.Bundled
import scientifik.plotly.assets.AssetsProvidingMode.Online


internal interface AssetsLocator {

    operator fun invoke(assetUri: String): String

    /**
     * Use [AssetLocator.of()][AssetsLocator.Companion.of]
     * to build new instance.
     */
    companion object
}

/**
 * Create new [AssetsLocator] instance.
 *
 * Usage:
 *
 * ```
 * val a = AssetLocator.of(AssetsProvidingType.Offline)
 *
 * html {
 *   img { src = a("https://cataas.com/cat") }
 * }
 * ```
 */
internal fun AssetsLocator.Companion.of(mode: AssetsProvidingMode) = when (mode) {
    Online -> TransparentAssetsLocator()
    Bundled -> DataUriAssetsLocator()
}

private class TransparentAssetsLocator : AssetsLocator {
    override fun invoke(assetUri: String): String = assetUri
}

private class DataUriAssetsLocator : AssetsLocator {

    override fun invoke(assetUri: String): String {
        val content = encodeBase64(
            fetchContent(assetUri))
        val mime = detectMime(assetUri)

        return "data:$mime;base64,$content"
    }

    private fun detectMime(uri: String): String {
        @Suppress("MoveVariableDeclarationIntoWhen")
        val extension = uri.substringAfterLast('.')

        return when (extension) {
            "css" -> "text/css"
            "js" -> "text/javascript"

            "jpeg", "jpg" -> "image/jpeg"
            "png" -> "image/png"
            "svg" -> "image/svg+xml"

            else -> TODO("MIME for '.$extension' is not found.")
        }
    }
}


