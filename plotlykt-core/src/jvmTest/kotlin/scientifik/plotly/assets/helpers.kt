package scientifik.plotly.assets

import java.io.File


internal fun testFileUri(file: String): String {
    val path = File("src/jvmTest/resources/$file")
        .normalize()
        .absolutePath
    return "file://$path"
}
