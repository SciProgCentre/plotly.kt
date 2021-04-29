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

**TODO**

## What about classic notebook?

**TODO**