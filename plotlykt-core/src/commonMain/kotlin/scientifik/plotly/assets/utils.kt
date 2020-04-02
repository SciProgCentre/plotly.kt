package scientifik.plotly.assets


internal expect fun encodeBase64(content: ByteArray): String

internal expect fun fetchContent(uri: String): ByteArray
