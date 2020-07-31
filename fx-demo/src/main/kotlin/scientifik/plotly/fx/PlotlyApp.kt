package scientifik.plotly.fx

import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.SelectionMode
import tornadofx.*

class PlotlyApp : App(PlotlyView::class)

fun main(args: Array<String>) {
    launch<PlotlyApp>(args)
}

class PlotlyView : View("Hello PlotlyFX") {
    private val controller: PlotlyController by inject()
    private val hide = SimpleBooleanProperty(false)
    override val root = hbox {
        listview(controller.pages) {
            selectionModel.selectionMode = SelectionMode.SINGLE
            selectionModel.selectedItemProperty().addListener(
                ChangeListener { _, _, value ->
                    hide.value = value != "Dynamic"
                    controller.displayPage(value)
                }
            )
        }
        vbox {
            stackpane {
                webview {
                    engine.setOnError {
                        log.warning(it.message)
                    }
                    engine.setOnAlert {
                        log.info(it.data)
                    }

                    controller.address.onChange {
                        if (it != null) {
                            engine.load(it)
                        }
                    }
                }
                progressindicator {
                    hiddenWhen(controller.address.isNotEmpty)
                }
            }
            slider(1.0, 100.0, 1.0) {
                hiddenWhen(hide)
                isShowTickLabels = true
                isShowTickMarks = true
                controller.scale.bind(valueProperty())
            }
        }
    }

    init {
        controller.startServer()
    }
}