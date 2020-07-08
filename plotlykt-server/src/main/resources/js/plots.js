/**
 * Create plot directly
 * @param target
 * @param data
 * @param layout
 */
function createPlot(target, data, layout) {
    Plotly.newPlot(target, data, layout, {showSendToCloud: true});
}

/**
 * Request and parse json from given address
 * @param url
 * @param callback
 */
function getJSON(url, callback) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'json';
    xhr.onload = function () {
        let status = xhr.status;
        if (status === 200) {
            callback(null, xhr.response);
        } else {
            callback(status, Json.parse(xhr.response));
        }
    };
    xhr.send();
}

/**
 * Create a plot taking data from given url
 * @param target {string} element id for plot
 * @param from {string} json server url
 * @return {JSON}
 */
function createPlotFrom(target, from) {
    getJSON(from, function (err, json) {
        if (err !== null) {
            alert('Something went wrong: ' + err);
        } else {
            Plotly.newPlot(target, json.data, json.layout, {showSendToCloud: true});
        }
    });
}

/**
 * Update a plot taking data from given url
 * @param target {string} element id for plot
 * @param from {string} json server url
 * @return {JSON}
 */
function updatePlotFrom(target, from) {
    getJSON(from, function (err, json) {
        if (err !== null) {
            alert('Something went wrong: ' + err);
        } else {
            Plotly.react(target, json.data, json.layout);
        }
    });
}

/**
 * Start pull updates with regular requests from client side
 * @param target
 * @param from
 * @param millis
 */
function startPull(target, from, millis){
    let action = function(){
      updatePlotFrom(target,from)
    };
    window.setInterval(action, millis)
}

/**
 * Start push updates via websocket
 * @param id {string} element id for plot
 * @param ws {string} a websocket address
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
                Plotly.relayout(target, json.content)
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
        socket.onclose = function () { }; // disable onclose handler first
        socket.close();
    };
}

