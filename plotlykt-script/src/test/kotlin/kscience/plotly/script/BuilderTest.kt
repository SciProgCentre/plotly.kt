package kscience.plotly.script

import kscience.plotly.Plotly
import kscience.plotly.makeFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BuilderTest{
    @Test
    fun testBuilderFromString(){
        val string = javaClass.getResource("/customPage.plotly.kts").readText()
        val page = Plotly.page(string)
        page.render()
    }
}