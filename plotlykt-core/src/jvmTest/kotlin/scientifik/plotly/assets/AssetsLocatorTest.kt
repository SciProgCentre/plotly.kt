package scientifik.plotly.assets

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import scientifik.plotly.assets.AssetsProvidingMode.Bundled
import scientifik.plotly.assets.AssetsProvidingMode.Online


class AssetsLocatorTest : StringSpec({

    "Online assets locator" {
        val testData = listOf(
            "https://www.mozilla.org/",
            "https://www.mozilla.org/logo.png"
        ).map(::row).toTypedArray()

        val locator = AssetsLocator.of(Online)

        forAll(*testData) { url ->
            locator(url) shouldBe url
        }
    }

    "Bundled assets locator" {
        val locator = AssetsLocator.of(Bundled)

        locator(testFileUri("pixel.png")) shouldBe "data:image/png;base64," +
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
    }
})
