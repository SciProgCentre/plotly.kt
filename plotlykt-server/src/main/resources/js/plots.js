/**
 * Create plot directly
 * @param id
 * @param data
 * @param layout
 */
function createPlot(id, data, layout) {
    Plotly.newPlot(id, data, layout, {showSendToCloud: true});
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


    fetch(url,{
        method: 'GET',
        headers: {
            Accept: 'application/json',
        }
    })
        .then(handleErrors)
        .then(response => response.json())
        .then(json => callback(json))
        .catch(error => console.log(error));
}

/**
 * Create a plot taking data from given url
 * @param id {string} element id for plot
 * @param from {URL} json server url
 * @return {JSON}
 */
function createPlotFrom(id, from) {
    getJSON(from, json => Plotly.newPlot(id, json.data, json.layout, {showSendToCloud: true}));
}

/**
 * Update a plot taking data from given url
 * @param id {string} element id for plot
 * @param from {URL} json server url
 * @return {JSON}
 */
function updatePlotFrom(id, from) {
    getJSON(from, json=> Plotly.react(id, json.data, json.layout));
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
        console.log("A connection with server established");
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
        if (json.plotId === id) {
            if (json.contentType === "layout") {
                Plotly.relayout(id, json.content)
            } else if (json.contentType === "trace") {
                let content = json.content;
                //This is done to satisfy plotly requirements of arrays-in-arrays for data
                if (content.hasOwnProperty('x')) {
                    content.x = [content.x]
                }
                if (content.hasOwnProperty('y')) {
                    content.y = [content.y]
                }
                Plotly.restyle(id, content, json['trace']);
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

