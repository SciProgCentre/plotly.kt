/**
 * Use requireJS to resolve plotly if it is available
 * @param action
 */
function withPlotly(action) {
    if (typeof require === "undefined") {
        return action(Plotly);
    } else {
        require(["plotly"], plotly => action(plotly));
    }
}

/**
 * Request and parse json from given address
 * @param url {URL}
 * @param callback
 * @return Promise<Json>
 */
function getJSON(url, callback) {

    function handleErrors(response) {
        if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
    }

    try {
        fetch(url, {
            method: 'GET',
            headers: {
                Accept: 'application/json',
            }
        })
            .then(handleErrors)
            .then(response => response.json())
            .then(json => callback(json))
            .catch(error => console.log(error));
    } catch (e) {
        alert("Fetch of plot data failed with error: " + e)
    }
}

/**
 * Create a plot taking data from given url
 * @param id {string} element id for plot
 * @param from {URL} json server url
 * @param config {object} plotly configuration
 * @return {JSON}
 */
function createPlotFrom(id, from, config) {
    getJSON(from, json => withPlotly(plotly => plotly.react(id, json.data, json.layout, config)));
}

/**
 * Update a plot taking data from given url
 * @param id {string} element id for plot
 * @param from {URL} json server url
 * @return {JSON}
 */
function updatePlotFrom(id, from) {
    getJSON(from, json => withPlotly(plotly => plotly.react(id, json.data, json.layout)));
}

/**
 * Start pull updates with regular requests from client side
 * @param id {string}
 * @param from
 * @param millis
 */
function startPull(id, from, millis) {
    let action = function () {
        updatePlotFrom(id, from)
    };
    window.setInterval(action, millis)
}

/**
 * Start push updates via websocket
 * @param id {string} element id for plot
 * @param ws {URL} a websocket address
 */
function startPush(id, ws) {
    let socket = new WebSocket(ws);

    socket.onopen = function () {
        console.log("[Plotly.kt] A connection for plot with id = " + id + " with server established on " + ws);
    };

    socket.onclose = function (event) {
        if (event.wasClean) {
            console.log("The connection with server is closed");
        } else {
            console.log("The connection with server is broken"); // Server process is dead
        }
        console.log('Code: ' + event.code + ' case: ' + event.reason);
    };

    socket.onerror = function (error) {
        alert("Error " + error.message);
    };

    socket.onmessage = function (event) {
        //console.log('got message: ' + event.data);
        let json = JSON.parse(event.data);
        //TODO check if plotly is initialized in a cell
        if (json.plotId === id) {
            if (json.contentType === "layout") {
                withPlotly(plotly => plotly.relayout(id, json.content));
            } else if (json.contentType === "trace") {
                let content = json.content;
                //This is done to satisfy plotly requirements of arrays-in-arrays for data
                if (content.hasOwnProperty('x')) {
                    content.x = [content.x];
                }
                if (content.hasOwnProperty('y')) {
                    content.y = [content.y];
                }
                if (content.hasOwnProperty('z')) {
                    content.z = [content.z];
                }
                withPlotly(plotly => plotly.restyle(id, content, json['trace']));
            }
        }
    };

    //gracefully close socket just in case
    window.onbeforeunload = function () {
        console.log("Gracefully closing socket");
        socket.onclose = function () {
        }; // disable onclose handler first
        socket.close();
    };
}

