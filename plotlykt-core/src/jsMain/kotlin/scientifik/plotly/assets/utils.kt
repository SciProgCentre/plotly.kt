package scientifik.plotly.assets

import kotlin.browser.window

internal actual fun encodeBase64(content: ByteArray): String =
    window.btoa(content.decodeToString())

internal actual fun fetchContent(uri: String): ByteArray {
    TODO()
    /*val request = XMLHttpRequest()
    request.open("GET", uri, async = false)
    request.responseType = XMLHttpRequestResponseType.ARRAYBUFFER

    request.onload = {
        val arr = Uint8Array(request.response as ArrayBuffer)
        arr
        return ByteArray(arr)
    }
    request.send()*/
}
