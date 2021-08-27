package geo

import io.dfValues
import krangl.DataFrame
import krangl.readCSV
import space.kscience.dataforge.meta.configure
import space.kscience.dataforge.meta.set
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.DragMode
import space.kscience.plotly.trace

private val df = DataFrame.readCSV("https://raw.githubusercontent.com/plotly/datasets/master/2015_06_30_precipitation.csv")

//https://plotly.com/javascript/mapbox-layers/#openstreetmap-tiles-no-token-needed
fun main() {
    val plot = Plotly.plot {
        trace {
            meta["type"] = "scattermapbox"
            meta["text"] = df["Globvalue"].dfValues()
            meta["lon"] = df["Lon"].dfValues()
            meta["lat"] = df["Lat"].dfValues()
            marker {
                color("fuchsia")
                size = 4
            }
        }

        layout {
            title = "OpenStreetMap with markers"
            dragmode = DragMode.zoom
            //margin { r = 0; t= 0; b= 0; l= 0 }
            configure {
                "mapbox" put {
                    "style" put "open-street-map"
                    "center" put {
                        "lat" put 38
                        "lon" put -90
                    }
                    "zoom" put 3
                }
            }
        }
    }
    plot.makeFile()
}