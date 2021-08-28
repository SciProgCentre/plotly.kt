package geo

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.geo.LocationMode
import space.kscience.plotly.models.geo.choroplethMapBox
import space.kscience.plotly.models.geo.openStreetMap
import kotlin.random.Random


fun main() {
    val regionRange = (369..396)

    val plot = Plotly.plot {
        choroplethMapBox {
            geojsonUrl =
                "https://raw.githubusercontent.com/isellsoap/deutschlandGeoJSON/main/4_kreise/4_niedrig.geo.json"
            locationmode = LocationMode.`geojson-id`
            text.strings = regionRange.map { it.toString() }
            locations.numbers = regionRange
            z.numbers = regionRange.map { Random.nextDouble(1.0, 10.0) }
            opacity = 0.3
        }

        layout {
            title = "Geojson demo"
            height = 800
        }

        openStreetMap{
            center {
                lat = 51.05
                lon = 13.73
            }
            zoom = 7.0
        }
    }
    plot.makeFile()
}