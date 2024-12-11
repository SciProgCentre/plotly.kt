package space.kscience.plotly

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import org.w3c.dom.url.URL
import org.w3c.fetch.RequestInit
import kotlin.js.Promise
import kotlin.js.json

@JsExport
public object PlotlyConnect {

    /**
     * Wait for the Plotly library to be loaded
     */
    private fun withPlotly(action: PlotlyJs.() -> Unit) {
        if (jsTypeOf(PlotlyJs) !== "undefined") {
            action(PlotlyJs);
        } else {
            val promiseOfPlotly: Promise<PlotlyJs> = window.asDynamic().promiseOfPlotly as Promise<PlotlyJs>
            if (jsTypeOf(promiseOfPlotly) != "undefined") {
                promiseOfPlotly.then { action(PlotlyJs) }
            } else {
                console.warn("Plotly not defined. Loading the script from CDN")
                window.asDynamic().promiseOfPlotly = Promise<PlotlyJs> { resolve, reject ->
                    val plotlyLoaderScript = document.createElement("script") as HTMLScriptElement
                    plotlyLoaderScript.src = "https://cdn.plot.ly/plotly-2.29.0.min.js"
                    plotlyLoaderScript.type = "text/javascript"
                    plotlyLoaderScript.onload = {
                        resolve(PlotlyJs)
                        action(PlotlyJs)
                    }
                    plotlyLoaderScript.onerror = { error, _, _, _, _ ->
                        console.error(error);
                        reject(error as Throwable)
                    }
                    document.head?.appendChild(plotlyLoaderScript);
                }
            }
        }
    }

    /**
     * Request and parse json from given address
     * @param url {URL}
     * @param callback
     */
    private fun getJSON(url: URL, callback: (dynamic) -> Unit) {
        try {
            window.fetch(
                url,
                RequestInit(
                    method = "GET",
                    headers = json("Accept" to "application/json")
                )
            ).then { response ->
                if (!response.ok) {
                    error("Fetch request failed with error: ${response.statusText}")
                } else {
                    response.json().then(callback)
                }
            }.catch { error -> console.log(error) }
        } catch (e: Exception) {
            window.alert("Fetch of plot data failed with error: $e")
        }
    }

    public fun makePlot(
        graphDiv: Element,
        data: Array<dynamic>,
        layout: dynamic,
        config: dynamic,
    ) {
        withPlotly { newPlot(graphDiv, data, layout, config) }
    }

    /**
     * Create a plot taking data from given url
     * @param id {string} element id for plot
     * @param from {URL} json server url
     * @param config {object} plotly configuration
     */

    public fun createPlotFrom(id: String, from: URL, config: dynamic = {}) {
        getJSON(from) { json ->
            val element = document.getElementById(id) as HTMLElement
            withPlotly { newPlot(element, json.data, json.layout, config) }
        }
    }

    /**
     * Update a plot taking data from given url
     * @param id {string} element id for plot
     * @param from {URL} json server url
     * @return {JSON}
     */
    public fun updatePlotFrom(id: String, from: URL) {
        getJSON(from) { json ->
            val element = document.getElementById(id) as HTMLElement
            withPlotly { react(element, json.data, json.layout) }
        }
    }

    /**
     * Start pull updates with regular requests from client side
     * @param id {string}
     * @param from
     * @param millis
     */
    public fun startPull(id: String, from: URL, millis: Int) {
        window.setInterval({ updatePlotFrom(id, from) }, millis)
    }

    /**
     * Start push updates via websocket
     * @param id {string} element id for plot
     * @param ws {URL} a websocket address
     */
    public fun startPush(id: String, ws: String) {
        val element = document.getElementById(id) as HTMLElement
        val socket = WebSocket(ws)

        socket.onopen = {
            console.log("[Plotly.kt] A connection for plot with id = $id with server established on $ws")
        }

        socket.onclose = { event ->
            event as CloseEvent
            if (event.wasClean) {
                console.log("The connection with server is closed")
            } else {
                console.log("The connection with server is broken") // Server process is dead
            }
            console.log("Code: ${event.code} case: ${event.reason}")
        }

        socket.onerror = { error ->
            error as ErrorEvent
            console.error("Plotly push update error: " + error.message)
            socket.close()
        }

        socket.onmessage = { event ->
            val json: dynamic = JSON.parse(event.data.toString())
            if (json.plotId === id) {
                if (json.contentType === "layout") {
                    withPlotly { relayout(element, json.content) }
                } else if (json.contentType === "trace") {
                    withPlotly { restyle(element, json.content, json.trace) }
                }
            }
        }

        //gracefully close the socket just in case
        window.onbeforeunload = {
            console.log("Gracefully closing socket")
            socket.close()
            null
        }
    }
}

public fun main() {
    window.asDynamic().plotlyConnect = PlotlyConnect
    window.asDynamic().Plotly = PlotlyJs
}