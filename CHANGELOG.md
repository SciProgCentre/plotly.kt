# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added

### Changed
- Serialization API is encapsulated (not longer exposed) in order to provide compatibility with new serialization.

### Deprecated

### Removed

### Fixed

### Security
## [0.2.0]


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
