package scientifik.plotly.assets

import java.net.URI
import java.util.Base64

internal actual fun encodeBase64(content: ByteArray) = Base64
    .getEncoder()
    .encodeToString(content)

internal actual fun fetchContent(uri: String) = URI
    .create(uri)
    .toURL()
    .readBytes()
