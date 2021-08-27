package geo

import io.invoke
import krangl.DataFrame
import krangl.readCSV
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.geo.GeoScope
import space.kscience.plotly.models.geo.LocationMode
import space.kscience.plotly.models.geo.choropleth
import space.kscience.plotly.models.geo.geo

private val df = DataFrame.readCSV("https://raw.githubusercontent.com/plotly/datasets/master/2014_usa_states.csv")

//https://plotly.com/javascript/choropleth-maps/#choropleth-map-of-2014-us-population-by-state
fun main() {
    val plot = Plotly.plot {
        choropleth {
            locationmode = LocationMode.`USA-states`
            text(df["State"])
            locations(df["Postal"])
            z(df["Population"])
        }

        layout {
            title = "2014 US Popultaion by State"
            geo{
                scope = GeoScope.usa
                countrycolor(255,255,255)
                landcolor(217, 217, 217)
                showland = true
                lakecolor(255, 255, 255)
                showlakes = true
                subunitcolor(255,255,255)
            }
        }
    }
    plot.makeFile()
}