package space.kscience.plotly

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

/**
 * Create a standalone html with the plot
 * @param path the reference to html file. If null, create a temporary file
 * @param show if true, start the browser after file is created
 * @param config represents plotly frame configuration
 */
@UnstablePlotlyAPI
public fun Plot.makeFile(
    path: Path,
    config: PlotlyConfig = PlotlyConfig(),
) {
    FileSystem.SYSTEM.write(path, true) {
        writeUtf8(toHTML(cdnPlotlyHeader, config = config))
    }
}

@UnstablePlotlyAPI
public fun Plot.makeFile(
    path: String,
    config: PlotlyConfig = PlotlyConfig(),
) {
    makeFile(path.toPath(), config)
}

/**
 * Export a page html to a file.
 */
@UnstablePlotlyAPI
public fun PlotlyPage.makeFile(path: Path) {
    FileSystem.SYSTEM.write(path, true) {
        writeUtf8(render())
    }
}

@UnstablePlotlyAPI
public fun PlotlyPage.makeFile(
    path: String,
) {
    makeFile(path.toPath())
}