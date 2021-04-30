# Plotly.kt Jupyter kernel integration
Being a JavaScript library with a Kotlin-JVM backend, Plotly.kt is ideally suited for [Jupyter kotlin kernel](https://github.com/Kotlin/kotlin-jupyter) integration. The integration is supported in two modes:

* Static rendering in [plotlykt-jupyter](../../plotlykt-jupyter) module.
* Dynamic updates processing via [plotlykt-server](../../plotlykt-server) module.

## Loading the library

* Install or update [Jupyter kotlin kernel](https://github.com/Kotlin/kotlin-jupyter) to version `0.9` or later.
* Launch kotlin kernel in `jupyter lab` (classic jupyter notebook is not supported).
* Use `@file:DependsOn("space.kscience:plotlykt-jupyter:$plotlyVersion")` annotation directive to load library, where `$plotlyVersion` is the required version of the library. This approach uses [Kotlin jupyter notebook API](https://github.com/Kotlin/kotlin-jupyter/blob/master/docs/libraries.md#Integration-using-Kotlin-API). For dynamic version one should replace `plotlykt-server` instead of `plotlykr-jupyter`.
* Alternatively one could use `%use plotly` [directive](https://github.com/Kotlin/kotlin-jupyter#supported-libraries). For dynamic version one should replace `plotly-server` instead of `plotly`.

## Automatic rendering

The integration automatically imports `space.kscience.plotlykt.*` and `space.kscience.plotlykt.models.*` and provides automatic rendering for three basic plotly objects: `Plot`, `PlotlyFragment` and `PlotlyPage`. One needs to return those object as result of last expressions in a cell in order to automatically render them.

## Dynamic update server

The dynamic server uses three-way communication to provide updates for the jupyter cell content:

* Jupyter kernel server
* Browser Plotly API (websockets)
* Ktor server which provides both static data and updates via web-sockets.

When the plot is placed into the page, its initial data is embedded into the page as plotly-compatible JSON alongside the path to data server and updates server.

The resulting HTML looks like this:
```html
<div id="space.kscience.plotly.Plot@502ed4cc">
    <script>
        makePlot(
           'space.kscience.plotly.Plot@502ed4cc',
            [/*plot data*/],
            {/*layout*/},
            {}
        );
        startPush('space.kscience.plotly.Plot@502ed4cc', 'ws://127.0.0.1:3872//ws/space.kscience.plotly.Plot@502ed4cc');
    </script>
</div>
```

The changes in the back-end model are automatically collected and sent to the notebook with fixed intervals (if present).
The interval is `100 ms` by default and could be changed via `plotly.updateInterval = 200` line. The server is restarted on interval change.

The local port used for the server (default is `8882`) also could be changed via configuration object (`plotly.port = 8884`). After port change, all plots that use dynamic updates must be re-created to ensure they use correct update location.

## What about classic notebook?

At this moment only Jupyter lab is supported. Classic notebook does not work well because it does not allow loading JS resources globally after the notebook is started. We will research the possibility to support classic notebook as well as other IPython kernels in the future.