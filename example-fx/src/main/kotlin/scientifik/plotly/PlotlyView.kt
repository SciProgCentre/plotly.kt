package scientifik.plotly

import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.SelectionMode
import javafx.scene.web.WebView
import tornadofx.*

class PlotlyView : View("Hello PlotlyFX") {
    private val controller: PlotlyController by inject()
    private val webBrowser = WebView()
    private val hide = SimpleBooleanProperty(true)
    override val root = hbox {
        listview(controller.plots) {
            selectionModel.selectionMode = SelectionMode.SINGLE
            selectionModel.selectedItemProperty().addListener(
                ChangeListener { observable, oldValue, newValue ->
                    hide.value = newValue.name != "Dynamic"
                    webBrowser.engine.load(newValue.address)
                }
            )
            cellFormat {
                text = it.name
            }
        }
        vbox {
            add(webBrowser)
            add(
                slider(1.0, 100.0, 1.0) {
                    hiddenWhen(hide)
                    isShowTickLabels = true
                    isShowTickMarks = true
                    controller.changeScale(value)
                    valueProperty().addListener(
                        ChangeListener { observable, oldValue, newValue ->
                            controller.changeScale(newValue)
                        }
                    )
                }
            )
        }
    }
}