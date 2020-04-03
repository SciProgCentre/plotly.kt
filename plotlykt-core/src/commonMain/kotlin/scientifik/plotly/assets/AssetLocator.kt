package scientifik.plotly.assets

import scientifik.plotly.assets.AssetsInclusion.BundledAssets
import scientifik.plotly.assets.AssetsInclusion.LocalAssets
import scientifik.plotly.assets.AssetsInclusion.OnlineAssets


internal interface AssetLocator {

    operator fun invoke(assetUri: String): String

    /**
     * Use [AssetLocator.create()][AssetLocator.Companion.create]
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
 * val a = AssetLocator.create()
 *
 * html {
 *   img { src = a("https://cataas.com/cat") }
 * }
 * ```
 */
internal fun AssetLocator.Companion.create(
    selfContained: Boolean = false
): AssetLocator =
    if (selfContained)
        DataUriAssetLocator()
    else
        TransparentAssetLocator()

enum class AssetsInclusion {
    OnlineAssets,
    BundledAssets,
    LocalAssets
}

internal fun AssetsInclusion.createLocator() = when (this) {
    OnlineAssets -> TransparentAssetLocator()
    BundledAssets -> DataUriAssetLocator()
    LocalAssets -> TODO()
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
            else -> TODO("MIME for '.$extension' is not found.")
        }
    }
}


