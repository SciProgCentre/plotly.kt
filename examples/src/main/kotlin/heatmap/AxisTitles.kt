package heatmap

import space.kscience.dataforge.meta.invoke
import space.kscience.dataforge.values.Value
import space.kscience.plotly.Plotly
import space.kscience.plotly.heatmap
import space.kscience.plotly.makeFile
import space.kscience.plotly.palettes.Xkcd


/**
 * - Heatmap with custom colorscale
 * - Add axis label
 * - Use XKCD color palette
 * - Change size of the plot
 */
fun main() {
    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December")
    val years = 1949..1961
    val flights = listOf(
            listOf(112, 115, 145, 171, 196, 204, 242, 284, 315, 340, 360, 417), /* January */
            listOf(118, 126, 150, 180, 196, 188, 233, 277, 301, 318, 342, 391), /* February */
            listOf(132, 141, 178, 193, 236, 235, 267, 317, 356, 362, 406, 419), /* March */
            listOf(129, 135, 163, 181, 235, 227, 269, 313, 348, 348, 396, 461), /* April */
            listOf(121, 125, 172, 183, 229, 234, 270, 318, 355, 363, 420, 472), /* May */
            listOf(135, 149, 178, 218, 243, 264, 315, 374, 422, 435, 472, 535), /* June */
            listOf(148, 170, 199, 230, 264, 302, 364, 413, 465, 491, 548, 622), /* July */
            listOf(148, 170, 199, 242, 272, 293, 347, 405, 467, 505, 559, 606), /* August */
            listOf(136, 158, 184, 209, 237, 259, 312, 355, 404, 404, 463, 508), /* September */
            listOf(119, 133, 162, 191, 211, 229, 274, 306, 347, 359, 407, 461), /* October */
            listOf(104, 114, 146, 172, 180, 203, 237, 271, 305, 310, 362, 390), /* November */
            listOf(118, 140, 166, 194, 201, 229, 278, 306, 336, 337, 405, 432)  /* December */)
    val customColorscale = listOf(listOf(0, Xkcd.POWDER_BLUE), listOf(1, Xkcd.PURPLE))

    val plot = Plotly.plot {
        heatmap {
            x.set(years)
            y.set(months)
            z.set(flights)
            colorscale = Value.of(customColorscale)
        }

        layout {
            width = 900
            height = 900
            xaxis {
                title = "Years"
            }
            title = "Heatmap of Flight Density from 1949 to 1961"
        }
    }

    plot.makeFile()
}