package scientifik.plotly.assets

import scientifik.plotly.assets.AssetsProvidingType.Bundled
import scientifik.plotly.assets.AssetsProvidingType.Online


internal interface AssetLocator {

    operator fun invoke(assetUri: String): String

    /**
     * Use [AssetLocator.of()][AssetLocator.Companion.of]
     * to build new instance.
     */
    companion object
}

/**
 * Create new [AssetLocator] instance.
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
internal fun AssetLocator.Companion.of(type: AssetsProvidingType) = when (type) {
    Online -> TransparentAssetLocator()
    Bundled -> DataUriAssetLocator()
}

private class TransparentAssetLocator : AssetLocator {
    override fun invoke(assetUri: String): String = assetUri
}

private class DataUriAssetLocator : AssetLocator {

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


