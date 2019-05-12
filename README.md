[ ![Bintray](https://api.bintray.com/packages/mipt-npm/scientifik/plotlykt-core/images/download.svg) ](https://bintray.com/mipt-npm/scientifik/plotlykt-core/_latestVersion)

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

# Planned features

* Other Plotly features
* Full JS support
* Static SVG export via orca
* Dash module
