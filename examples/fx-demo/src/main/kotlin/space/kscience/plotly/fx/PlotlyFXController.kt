package space.kscience.plotly.fx

import io.ktor.server.engine.ApplicationEngine
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.*
import tornadofx.*
import java.net.URI

class PlotlyFXController : Controller() {

    val scale = SimpleIntegerProperty(1)

    private var server: ApplicationEngine? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun startServer() {
        GlobalScope.launch(Dispatchers.Default) {
            log.info("Starting server")
            server = serve(scale)
            log.info("Server started")
            runLater {
                displayPage(pages.first())
            }
        }
    }

    val pages = observableListOf(
        "Dynamic",
        "Static"
    )

    val address = SimpleStringProperty()

    fun displayPage(page: String) = runBlocking {
        server?.let {
            val connector = it.resolvedConnectors().first()
            val uri = URI("http", null, connector.host, connector.port, null, null, null)
            address.set("$uri/$page")
        }
    }
}