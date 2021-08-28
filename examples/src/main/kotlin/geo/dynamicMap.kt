package geo

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.models.geo.LocationMode
import space.kscience.plotly.models.geo.choroplethMapBox
import space.kscience.plotly.models.geo.json.GeoJsonFeatureCollection
import space.kscience.plotly.models.geo.json.combine
import space.kscience.plotly.models.geo.openStreetMap
import space.kscience.plotly.plot
import space.kscience.plotly.server.close
import space.kscience.plotly.server.pushUpdates
import space.kscience.plotly.server.serve
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

    val server = Plotly.serve {
        page { plotly ->
            plot(renderer = plotly) {
                choroplethMapBox {
                    geoJsonFeatures(features)
                    //Switch to geojson-id mode
                    locationmode = LocationMode.`geojson-id`

                    //Setup the background map
                    openStreetMap {
                        center {
                            lat = 51.05
                            lon = 13.73
                        }
                        zoom = 7.0
                    }      // Set hover text to region names
                    text.strings = features.map { it.getString("NAME_3")!! }
                    // Set displayed locations
                    locations.numbers = features.map { it.id!!.int }
                    // Set random values to locations
                    z.numbers = features.map { Random.nextDouble(1.0, 10.0) }
                    launch {
                        while (isActive){
                            delay(300)
                            z.numbers = features.map { Random.nextDouble(1.0, 10.0) }
                        }
                    }
                }

                layout {
                    title = "Geojson demo"
                    height = 800
                }
                embedData = true
                pushUpdates(100)
            }
        }
    }

    println("Press Enter to close server")
    while (readLine()?.trim() != "exit"){
        //wait
    }

    server.close()

}