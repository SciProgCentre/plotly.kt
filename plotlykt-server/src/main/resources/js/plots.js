/**
 * Create plot directly
 * @param target
 * @param data
 * @param layout
 */
function createPlot(target, data, layout) {
    Plotly.newPlot(target, data, layout, {showSendToCloud: true});
}

var getJSON = function(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'json';
    xhr.onload = function() {
        var status = xhr.status;
        if (status === 200) {
            callback(null, xhr.response);
        } else {
            callback(status, Json.parse(xhr.response));
        }
    };
    xhr.send();
};


/**
 * Create a plot taking data from given url
 * @param target {string} element id for plot
 * @param from {string} json server url
 */
function createPlotFrom(target, from){
    getJSON(from, function (err, json) {
        if (err !== null) {
            alert('Something went wrong: ' + err);
        } else {
            Plotly.newPlot(target, json.data,json.layout,{showSendToCloud: true});
        }
    });
}

/**
 *
 * @param target element id for plot
 * @param page the name of the current page to filter events
 * @param plot the name of the plot
 * @param ws {string} a websocket address
 */
function updatePlot(target, page, plot, ws) {
    let socket = new WebSocket(ws);

    socket.onopen = function () {
        console.log("A connection with server established");
    };

    socket.onclose = function (event) {
        if (event.wasClean) {
            console.log("The connection with server is closed");
        } else {
            console.log("The connection with server is broken"); // например, "убит" процесс сервера
        }
        console.log('Code: ' + event.code + ' case: ' + event.reason);
    };

    socket.onerror = function (error) {
        alert("Error " + error.message);
    };

    socket.onmessage = function (event) {
        //console.log('got message: ' + event.data);
        let json = JSON.parse(event.data);
        if (json.page === page && json.plot === plot) {
            if (json.contentType === "layout") {
                Plotly.relayout(target, json.content)
            } else if (json.contentType === "trace") {
                let content = json.content;
                //This is done to satisfy plotly requirements of arrays-in-arrays for data
                if(content.hasOwnProperty('x')){
                    content.x = [content.x]
                }
                if(content.hasOwnProperty('y')){
                    content.y = [content.y]
                }
                Plotly.restyle(target, content, json['trace']);
            }
        }
    };
}