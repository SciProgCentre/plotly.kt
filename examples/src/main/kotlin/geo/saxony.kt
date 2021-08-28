package geo

import kotlinx.serialization.json.int
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.geo.LocationMode
import space.kscience.plotly.models.geo.choroplethMapBox
import space.kscience.plotly.models.geo.json.GeoJsonFeatureCollection
import space.kscience.plotly.models.geo.json.combine
import space.kscience.plotly.models.geo.openStreetMap
import java.net.URL
import kotlin.random.Random


fun main() {

    //downloading GeoJson
    val geoJsonString =
        URL("https://raw.githubusercontent.com/isellsoap/deutschlandGeoJSON/main/4_kreise/4_niedrig.geo.json").readText()


    // Filtering GeoJson features and creating new feature set
    val features = GeoJsonFeatureCollection.parse(geoJsonString).filter {
        it.getString("NAME_1") == "Sachsen"
    }.combine()

    val plot = Plotly.plot {
        choroplethMapBox {
//          Use this for remote-loaded feature set
//            geojsonUrl =
//                "https://raw.githubusercontent.com/isellsoap/deutschlandGeoJSON/main/4_kreise/4_niedrig.geo.json"
            //load local in-memory feature set and switch to
            geoJsonFeatures(features)
            //Switch to geojson-id mode
            locationmode = LocationMode.`geojson-id`
            // Set hover text to region names
            text.strings = features.map { it.getString("NAME_3")!! }
            // Set displayed locations
            locations.numbers = features.map { it.id!!.int }
            // Set random values to locations
            z.numbers = features.map { Random.nextDouble(1.0, 10.0) }
        }

        layout {
            title = "Geojson demo"
            height = 800
        }

        //Setup the background map
        openStreetMap {
            center {
                lat = 51.05
                lon = 13.73
            }
            zoom = 7.0
        }
    }
    plot.makeFile()
}