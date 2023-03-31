package space.kscience.plotly

import okio.FileSystem
import okio.Path

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
        writeUtf8( toHTML(cdnPlotlyHeader, config = config))
    }
}