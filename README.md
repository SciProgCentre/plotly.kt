[![JetBrains Research](https://jb.gg/badges/research.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![DOI](https://zenodo.org/badge/186020000.svg)](https://zenodo.org/badge/latestdoi/186020000)
![Gradle build](https://github.com/mipt-npm/plotly.kt/workflows/Gradle%20build/badge.svg)

![Plotlykt logo](./docs/logo_text.svg)

## Release version

Plotly.kt core on Bintray: [ ![Bintray](https://api.bintray.com/packages/mipt-npm/scientifik/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/kscience/plotlykt-core/_latestVersion)

Plotly.kt ktor server on Bintray: [ ![Bintray](https://api.bintray.com/packages/mipt-npm/scientifik/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/kscience/plotlykt-server/_latestVersion)

## Development version

Plotly.kt core on Bintray: [ ![Bintray](https://api.bintray.com/packages/mipt-npm/dev/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/dev/plotlykt-core/_latestVersion)

Plotly.kt ktor server on Bintray: [ ![Bintray](https://api.bintray.com/packages/mipt-npm/dev/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/dev/plotlykt-server/_latestVersion)

## Compatibility note
The current `0.2.0` version of the library is compatible with kotlin 1.3 and kotlinx-serialization 0.20.0. It is not guaranteed to work with kotlin 1.4. The next major version (`0.3.x`) will rely on Kotlin 1.4 and will partially break compatibility with kotliin 1.3 (it will use IR for JS target).

# TL;DR
See [examples](./examples/src/main/kotlin).
See [original library samples](https://plotly.com/javascript/) to understand capabilities.

# Description

This project is developed to allow simple access to plotly functionality from kotlin-multiplatform. The API allows to create plotly configuration and render it as a plotly chart.

The library supports three drawable plot objects:
* `Plot` itself stands for a stand-alone plot frame. It requires external infrastructure to load appropriate JavaScript libraries.
* `PlotFragment` (JVM only) is an HTML fragment possibly including several plots. The API for html is provided by [kotlinx-html](https://github.com/Kotlin/kotlinx.html) library.
* `PlotlyPage` (JVM only) is a complete page, including body fragment and page headers (needed to load JavaScript part of Plotly).

The work with plotly graphs could be rendered in following modes:

## HTML page export
(JVM only) Export plot or a plot grid in a standalone html file, which
uses CDN based plotly distribution. This mode does not support updates.

See [staticPlot](./examples/src/main/kotlin/staticPlot.kt) and  
[customPage](./examples/src/main/kotlin/customPage.kt) for examples.

## Ktor-based server with dynamic updates
(JVM only) A Ktor CIO server with full multi-page and update capabilities.

See [simpleServer](./examples/src/main/kotlin/simpleServer.kt) and  
[dynamicServer](./examples/src/main/kotlin/dynamicServer.kt) for examples.

## Kotlin-JS
Plotly is a JavaScript library, yet it is convenient to have a type-safe API when using in with Kotlin-JS. The sample application is available in [js-demo](./js-demo) module. One should node that Plotly.kt for JS is not a zero-cost wrapper like TypeScript definitions, it maintains its own object structure, could generate stand-alone models and some internal optimizations.

## JavaFX browser
Plotly.kt could be run in a JavaFX browser. An example project is presented in [fx-demo](./fx-demo).

## Kotlin jupyter kernel (experimental)
Plotly.kt comes with (for now experimental) support for integration with [Kotlin Jupyter kernel](https://github.com/Kotlin/kotlin-jupyter).

The examples of the notebooks are shown in [notebooks](./notebooks) directory. There are two module descriptor `plotly.json` and `plotly-server.json` in the same directory. They should be loaded according to Kotlin kernel documentation (either copied to `~/jypyter_kotlin/libraries` or loaded directly).

The module `plotly` allows to render static plots in Jupyter. Jupyter lab is currently supported. Jupyter notebook (classic) is able to render only `PlotlyPage` objects, so one must convert plots to pages to be able to use notebook (see [demo notebook](./notebooks/plotlykt-demo-classic.ipynb)).

The module `plotly-server` adds server capabilities and allows to render dynamic plots in notebooks (see [demo notebook](./notebooks/plotlykt-server-demo.ipynb)). One must note that for dynamic pages, one must pass `renderer` parameter explicitly to plot like it is done in examples.

## Direct image render via Orca (experimental)
[Plotly Orca](https://github.com/plotly/orca) application allows direct rendering of plots (not fragments or pages) to raster of vector images.
`Plot.export` extension could be used to call it. It requires for orca to be installed in the system and available on the system path.

## Kotlin-scripting (experimental)
It is possible to separate script logic into stand-alone `plotly.kts` script file and generate an html from the command line. See [plotlykt-script](./plotlykt-script) module for details.

# The feature I need is not implemented!

There are three ways to solve it:
1. Contribute! It is easy! Just add a model you need.
2. Create a model you need in your project or add an extension. Since the inner model is dynamic, you can add features on flight.
3. You can dynamically add missing features directly into configuration
like it done in [unsupportedFeature](./examples/src/main/kotlin/unsupportedFeature.kt) example.

# Build and usage

In order to use the library, one needs to use following `gradle.kts` configuration:

```kotlin
plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/kscience")
}

dependencies {
    implementation("scientifik:plotlykt-server:0.2.0")
}
```

When using development versions (if version contains `dev`), one should also add
```
    maven("https://dl.bintray.com/mipt-npm/dev")
```
into the repository list.

If you do not need the server, then use plotlykt-core instead and remove `ktor` repository.

# Naming
The library keeps original Plotly API naming wherever it is possible. There are some usability shortcuts, usually provided via kotlin extensions, included in order to simplify user interaction. For example, `text` and `shape` extensions in the top level API.

Keeping the original naming sometimes causes clashes with Kotlin code style. For example enum names are unorthodox.

# Planned features

* Table widgets
* Serverside plot events
* Online plot editor
* Dynamic data
* Mathjax and latex support

# Contributions and thanks
* [Vasily Chernov](https://research.jetbrains.org/researchers/vchernov) - initial project foundation
* [Alexander Nozik](https://research.jetbrains.org/researchers/altavir) - dynamic core and server
* [Mikhail Zeleniy](https://research.jetbrains.org/researchers/gama_sennin) - basic models
* [Ekaterina Samorodova](https://github.com/ebsamorodova) (JBR-2020 summer internship) - Model refactoring, tutorials and `0.2` release.

The project was partially founded by JetBrains Research grant.
