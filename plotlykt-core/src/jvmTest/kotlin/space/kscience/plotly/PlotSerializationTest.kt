package space.kscience.plotly

import org.junit.jupiter.api.Test
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.asObservable
import space.kscience.dataforge.values.ListValue
import space.kscience.plotly.models.TraceType
import kotlin.test.assertEquals


class PlotSerializationTest {
    @Test
    fun deserialization() {
        val meta = MutableMeta {
            "data" put {
                "x" put ListValue(1, 2, 3)
                "y" put ListValue(5, 6, 7)
                "type" put "scatter"
            }
        }

        val plot = Plot(meta.asObservable())
        assertEquals(1, plot.data.size)
        assertEquals(TraceType.scatter, plot.data[0].type)
        assertEquals(1.0, plot.data[0].x.doubles[0])
    }
}