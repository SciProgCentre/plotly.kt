package space.kscience.plotly

import org.junit.jupiter.api.Test
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.toConfig
import space.kscience.plotly.models.TraceType
import kotlin.test.assertEquals


class PlotSerializationTest {
    @Test
    fun deserialization() {
        val meta = Meta {
            "data" put {
                "x" put listOf(1, 2, 3)
                "y" put listOf(5, 6, 7)
                "type" put "scatter"
            }
        }

        val plot = Plot(meta.toConfig())
        assertEquals(1, plot.data.size)
        assertEquals(TraceType.scatter, plot.data[0].type)
        assertEquals(1.0, plot.data[0].x.doubles[0])
    }
}