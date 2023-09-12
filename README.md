[![JetBrains Research](https://jb.gg/badges/research.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![DOI](https://zenodo.org/badge/186020000.svg)](https://zenodo.org/badge/latestdoi/186020000)
![Gradle build](https://github.com/mipt-npm/plotly.kt/workflows/Gradle%20build/badge.svg)
[![Kotlin JS IR supported](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow)](https://kotl.in/jsirsupported)

![Plotlykt logo](./docs/logo_text.svg)

## Artifact details

[![Maven Central](https://img.shields.io/maven-central/v/space.kscience/plotlykt-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22space.kscience%22%20AND%20a:%22plotlykt-core%22)

Dev builds and intermediate artifacts are available via `https://repo.kotlin.link` maven repository.

## Compatibility note
The current `$version` version of the library is compatible with kotlin 1.4 with JS-IR and kotlinx-serialization 1.1.0. The JVM part requires JVM 11 to run.

# TL;DR
See [examples](./examples/src/main/kotlin).
See [original library samples](https://plotly.com/javascript/) to understand capabilities.

# Description

This project is developed to allow simple access to plotly functionality from kotlin-multiplatform. The API allows to create plotly configuration and render it as a plotly chart.

The library supports three drawable plot objects:
* `Plot` itself stands for a stand-alone plot frame. It requires external infrastructure to load appropriate JavaScript libraries.
* `PlotFragment` is an HTML fragment possibly including several plots. The API for html is provided by [kotlinx-html](https://github.com/Kotlin/kotlinx.html) library.
* `PlotlyPage` is a complete page, including body fragment and page headers (needed to load JavaScript part of Plotly).

The work with plotly graphs could be rendered in following modes:

## HTML page export
(JVM and native) Export plot or page in a standalone html file, using CDN distribution or local JS file (JVM only). This mode does not support updates.

See [staticPlot](./examples/src/main/kotlin/staticPlot.kt) and  
[customPage](./examples/src/main/kotlin/customPage.kt) for examples.

## Ktor-based server with dynamic updates
(JVM only) A Ktor CIO server with full multi-page and update capabilities.

See [simpleServer](./examples/src/main/kotlin/simpleServer.kt) and  
[dynamicServer](./examples/src/main/kotlin/dynamicServer.kt) for examples.

## Kotlin-JS
Plotly is a JavaScript library, yet it is convenient to have a type-safe API when using in with Kotlin-JS. The sample application is available in [js-demo](./examples/js-demo) module. One should node that Plotly.kt for JS is not a zero-cost wrapper like TypeScript definitions, it maintains its own object structure, could generate stand-alone models and some internal optimizations.

**Plotly-kt does not support `LEGACY` JS target. Be sure to use [IR compiler](https://kotlinlang.org/docs/js-ir-compiler.html)**

## JavaFX browser
Plotly.kt could be run in a JavaFX browser. An example project is presented in [fx-demo](./fx-demo).

## Kotlin jupyter kernel
Plotly.kt comes with (beta-version) support for integration with [Kotlin Jupyter kernel](https://github.com/Kotlin/kotlin-jupyter). See details [here](./docs/tutorials/jupyter.md).

The examples of the notebooks are shown in [notebooks](./examples/notebooks) directory. Plotly.kt uses Kotlin jupyter notebook API for integration (available in kernel version `0.8.3.236` and later). In order to load the library together with automatic imports one need to simply load a library in a following way:

```kotlin
@file:Repository("https://repo.kotlin.link")
@file:DependsOn("space.kscience:plotlykt-jupyter:$version")
//@file:DependsOn("space.kscience:plotlykt-server:$version") // Use this one for sever integration.
```

The module `plotly` allows rendering static plots in Jupyter. Jupyter lab is currently supported. Jupyter notebook (classic) is able to render only `PlotlyPage` objects, so one must convert plots to pages to be able to use notebook (see [demo notebook](./examples/notebooks/plotlykt-demo.ipynb)).

The module `plotly-server` adds server capabilities and allows to render dynamic plots in notebooks (see [demo notebook](./examples/notebooks/plotlykt-server-demo.ipynb)). One must note that for dynamic pages, one must pass `renderer` parameter explicitly to plot like it is done in examples.

**IMPORTANT:** By default, Plotly-kt jupyter integration is configured to work with Jupyter Lab frontend, which renders all cells in the same page. Jupyter classic notebook and DataLore use cell isolation with iframes, so you will see blanks instead of plots. It could be fixed by switching into a notebook mode by running `Plotly.jupyter.notebook()` in a cell after plotly library is loaded. 

## Direct image render via Orca (experimental)
[Plotly Orca](https://github.com/plotly/orca) application allows direct rendering of plots (not fragments or pages) to raster of vector images.
`Plot.export` extension could be used to call it. It requires for orca to be installed in the system and available on the system path.

## Kotlin-scripting (experimental)
It is possible to separate script logic into stand-alone `plotly.kts` script file and generate an html from the command line. See [plotlykt-script](./plotlykt-script) module for details.

## Kotlin/Native (experimental)
Plotly model now fully supports Kotlin/Native. It means that you can use it to create a proper Plotly-based HTML file. You will still need browser to view it. You can use [native-demo](./examples/native-demo) example.

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
    maven("https://repo.kotlin.link")
}

dependencies {
    implementation("space.kscience:plotlykt-server:$version")
}
```


If you do not need the server, then use plotlykt-core instead.

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

