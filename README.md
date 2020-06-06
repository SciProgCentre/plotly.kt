Plotly.kt core on Bintray: [ ![Bintray](https://api.bintray.com/packages/mipt-npm/scientifik/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/scientifik/plotlykt-core/_latestVersion)

Plotly.kt ktor server on Bintray: [ ![Bintray](https://api.bintray.com/packages/mipt-npm/scientifik/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/scientifik/plotlykt-server/_latestVersion)

[![DOI](https://zenodo.org/badge/186020000.svg)](https://zenodo.org/badge/latestdoi/186020000)

# Description

This project is developed to allow simple access to plotly functionality from kotlin-multiplatform.
The API allows to create plotly configuration and render it as a plotly chart.

The work with plotly graphs could be done in following modes:

## HTML page export
(JVM only) Export plot or a plot grid in a standalone html file, which
uses CDN based plotly distribution. This mode does not support updates.

See [staticPlot](./examples/src/main/kotlin/staticPlot.kt) and  
[staticPage](./examples/src/main/kotlin/staticPage.kt) for examples.

## Ktor-based server with dynamic updates
(JVM only) A Ktor CIO server with full multi-page and update capabilites.

See [simpleServer](./examples/src/main/kotlin/simpleServer.kt) and  
[dynamicServer](./examples/src/main/kotlin/dynamicServer.kt) for examples.

# The feature I need is not implemented!

There are two ways to solve it:
1. Contribute! It is easy!
2. You can dynamically add missing features directly into configuration
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
    maven("https://dl.bintray.com/mipt-npm/scientifik")
    maven("https://dl.bintray.com/kotlin/ktor/")
}

dependencies {
    implementation("scientifik:plotlykt-server:0.1.2")
}
```

When using development versions (if version contains `dev`), one should also add
```
    maven("https://dl.bintray.com/mipt-npm/dev")
```
into the repository list.

If you do not need the server, then use plotlykt-core instead and remove `ktor` repository.

# Planned features

* Other Plotly features
* Full JS support
* Static SVG export via orca
* Dash module
