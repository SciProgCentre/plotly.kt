package geo

import io.invoke
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.io.readCSV
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.DragMode
import space.kscience.plotly.models.geo.mapbox
import space.kscience.plotly.models.geo.scattermapbox
import space.kscience.plotly.models.geo.useOpenStreetMap

private val df = DataFrame.readCSV(
    "https://raw.githubusercontent.com/plotly/datasets/master/2015_06_30_precipitation.csv"
)

//https://plotly.com/javascript/mapbox-layers/#openstreetmap-tiles-no-token-needed
fun main() {
    val plot = Plotly.plot {
        scattermapbox {
            text(df["Globvalue"])
            lon(df["Lon"])
            lat(df["Lat"])
            marker {
                color("fuchsia")
                size = 4
            }
        }

        layout {
            title = "OpenStreetMap with markers"
            dragmode = DragMode.zoom
            //margin { r = 0; t= 0; b= 0; l= 0 }
            mapbox {
                useOpenStreetMap()
                center {
                    lat = 38
                    lon = -90
                }
                zoom = 3.0
            }
        }
    }
    plot.makeFile()
}