# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## 0.5.3 - 2023-04-01

### Added
- API for background images (https://github.com/SciProgCentre/plotly.kt/issues/49)
- API for multiple Y axis (https://github.com/SciProgCentre/plotly.kt/issues/92)
- Native support
- `plotlykt module` with basic Geo API
- DataSourceHost/DataSourcePost to configure custom networks

### Changed
- Kotlin 1.8.20
- Moved renderers to JVM to avoid confusion with JS direct element rendering.
- DataForge 0.6
- Replaced krangl by Kotlin-DataFrame in examples
- Plotly server uses push strategy by default
- Renderers moved to common
- Moved to Ktor 2.0

### Deprecated
- Page layout. Use VisionForge for that.

### Removed
- Moved CORS to `Plotly.serve`

### Fixed
- Added a protective copy on reading doubleArray from TraceValues
- #85
- Rendering in JS that used backend HTML generation

## 0.5.0

### Changed
- Switch to DataForge 0.5
-

## 0.4.4

### Added
- Candlestick support
- Range builders for axis

### Changed
- build tools 0.10.0
- demo projects moved to examples

### Deprecated
- Direct usage of `range` in axis

### Fixed
- #80
- Plotly coordinate array wrap is moved to the server side

## 0.4.3

### Fixed
- Proper deserialization of single plot.
- A bug in jupyter lab visualization

## 0.4.2

### Added
- `automargin` property to `Axis` according to https://plotly.com/python/reference/layout/xaxis/#layout-xaxis-automargin

### Fixed
- Remove unnecessary `kotlinx-css` dependency.
- Added compatibility mode for legacy notebooks. Use `Plotly.jupyter.notebook()` call to enable legacy mode.

## 0.4.0

### Added
- Jupyter integration plugin for server
- Separate static plot integration module in `plotlykt-jupyter`
- Expanded JS demo
- Jupyter support goes beta

### Changed
- Package change (again) to `space.kscience`
- Build tools `0.9.5`
- Kotlin `1.5.0`
- HtmlFragment renamed to PlotlyHtmlFragment

### Removed
- Local bootstrap

### Fixed
- Incomplete coverage in JS (#70)

## 0.3.1

### Added
- Table widget implementation by @ArtificialPB
- Mathjax header promoted to stable
- Tabbed plots layout (experimental)
- Trace value builders for functions and ranges (experimental)

### Changed
- **Breaking API change!** Trace `text` replaced by `TraceValues`
- Moved to DataForge 0.3 API
- Kotlin 1.4.30
- **JVM-IR**
- Plot `Config` moved to constructor
- Replaced direct color accessor by a delegate

### Fixed
- https://github.com/mipt-npm/plotly.kt/issues/53
- Add JQuery to Bootstrap headers

## 0.3.0

### Changed
- Serialization API is encapsulated (not longer exposed) in order to provide compatibility with new serialization.
- Migration to Kotlin 1.4
- Minor breaking change in Plot to encapsulate serialization usage
- JS supports IR. LEGACY is not supported anymore.

### Fixed
- https://github.com/mipt-npm/plotly.kt/issues/51

## 0.2.0

### Added
- Experimental scripting support
- Static export via Orca
- Experimental Jupyter support
- Color palettes (T10 and XKCD)
- New parameters and classes in Trace, Legend, Layout
- Parameters description
- naming.md with decisions about parameters and methods names
- Error bars
- New scatter, contour, error plots  examples
- Interfaces with common parameters for some plots (Histogram, Contour, Heatmap)
- New parameters for different plots
- Add Z axis
- Tutorial about drawing sinus
- Loading resources using krangl
- TraceValues extension for krangl columns

### Changed
- Migrated from `scientifik` to `kscience`
- Refactored packages to better suit star import style
- Removed bootstrap dependency
- Examples colors changed and more cases for each example added
- Different types of plots were inherited from Trace
- Unnecessary constructors removed
- Change Title methods
- Project structure updated
- Removed other Plot functions
- Plot2D renamed to Plot
