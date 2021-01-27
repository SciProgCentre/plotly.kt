package table

import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.models.HorizontalAlign
import kscience.plotly.models.Table

/**
 * - basic table trace with customization.
 */
fun main() {
    val values = listOf(
        listOf("Salaries", "Office", "Merchandise", "Legal", "<b>TOTAL</b>"),
        listOf(1200000, 20000, 80000, 2000, 12120000),
        listOf(1300000, 20000, 70000, 2000, 130902000),
        listOf(1300000, 20000, 120000, 2000, 131222000),
        listOf(1400000, 20000, 90000, 2000, 14102000),
    )

    val labels = listOf(
            listOf("<b>EXPENSES</b>"), listOf("<b>Q1</b>"), listOf("<b>Q2</b>"), listOf("<b>Q3</b>"), listOf("<b>Q4</b>")
    )

    val table = Table {
        columnorder(0, 1, 2, 4, 3)
        columnwidth(10, 10, 30, 30, 20)
        header {
            this.values(labels)
            align(HorizontalAlign.left, HorizontalAlign.center)

            line {
                width = 1
                color("#506784")
            }
            fill {
                color("#119DFF")
            }
            font {
                family = "Arial"
                size = 12
                color("white")
            }
        }
        cells {
            this.values(values)
            align(HorizontalAlign.left, HorizontalAlign.center)
            line {
                width = 1
                color("#506784")
            }
            fill {
                colors(listOf("#25FEFD", "white"))
            }
            font {
                family = "Arial"
                size = 11
                color("#506784")
            }
        }
    }

    Plotly.plot { traces(table) }.makeFile()
}