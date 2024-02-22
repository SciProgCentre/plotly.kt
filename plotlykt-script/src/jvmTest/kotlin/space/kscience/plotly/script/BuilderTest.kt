package space.kscience.plotly.script

import org.junit.jupiter.api.Test
import space.kscience.plotly.Plotly

internal class BuilderTest {
    @Test
    fun testBuilderFromString() {
        val string = javaClass.getResource("/customPage.plotly.kts").readText()
        val page = Plotly.page(string)
        println(page.render())
    }
}