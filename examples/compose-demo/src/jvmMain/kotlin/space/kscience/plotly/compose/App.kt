package space.kscience.plotly.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.flow.MutableStateFlow

private val allowedPages = listOf(
    "Static",
    "Dynamic"
)

@Composable
fun App() {
    val scaleFlow = remember { MutableStateFlow(1f) }
    val scale by scaleFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val server = remember {
        scope.servePlots(scaleFlow)
    }

    val state = rememberWebViewStateWithHTMLData(staticPlot())

    val navigator = rememberWebViewNavigator()

    val loadingState = state.loadingState
    if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
            progress = loadingState.progress,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Row(Modifier.fillMaxSize()) {
        Column(Modifier.width(300.dp)) {
            Button({ navigator.loadHtml(staticPlot()) }, modifier = Modifier.fillMaxWidth()) {
                Text("Static")
            }
            Button({ navigator.loadUrl("http://localhost:7778/Dynamic") }, modifier = Modifier.fillMaxWidth()) {
                Text("Dynamic")
            }

            Slider(
                scale,
                { scaleFlow.value = it },
                valueRange = 0.1f..10f,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(Modifier.fillMaxSize()) {

            WebView(
                state = state,
                navigator = navigator,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun main() = application {
    KCEF.initBlocking(
        builder = {
            progress {
                onDownloading {
                    println("Download progress: $it%")
                }
            }
            release(true)
        }
    )
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            App()
        }
    }
}
