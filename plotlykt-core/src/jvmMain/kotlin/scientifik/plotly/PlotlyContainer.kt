package scientifik.plotly

import kotlinx.html.*

interface PlotlyContainer {
    fun FlowContent.renderPlot(
        plot: Plot,
        plotId: String = plot.toString(),
        config: PlotlyConfig = PlotlyConfig()
    ): Plot
}

object StaticPlotlyContainer : PlotlyContainer {
    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        div {
            id = plotId
            script {
                val tracesString = plot.data.toJsonString()
                val layoutString = plot.layout.toJsonString()
                unsafe {
                    //language=JavaScript
                    +"""

                    window.$PLOTLY_PROMISE_NAME.then( plotly =>{
                        plotly.react(
                            '$plotId',
                            $tracesString,
                            $layoutString,
                            $config
                        );
                    });
                    
                    """.trimIndent()
                }
            }
        }
        return plot
    }
}

fun FlowContent.plot(
    plot: Plot,
    plotId: String = plot.toString(),
    config: PlotlyConfig = PlotlyConfig(),
    container: PlotlyContainer = StaticPlotlyContainer
): Plot = with(container) {
    renderPlot(plot, plotId, config)
}

fun FlowContent.plot(
    plotId: String? = null,
    config: PlotlyConfig = PlotlyConfig(),
    container: PlotlyContainer = StaticPlotlyContainer,
    builder: Plot.() -> Unit
): Plot {
    val plot = Plot().apply(builder)
    return plot(plot, plotId ?: plot.toString(), config, container)
}
