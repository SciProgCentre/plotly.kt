package scientifik.plotly

import kotlinx.html.*

interface PlotlyContainer {
    fun FlowContent.renderPlot(
        plot: Plot2D,
        plotId: String = plot.toString(),
        config: PlotlyConfig = PlotlyConfig()
    ): Plot2D
}

object StaticPlotlyContainer : PlotlyContainer {
    override fun FlowContent.renderPlot(plot: Plot2D, plotId: String, config: PlotlyConfig): Plot2D {
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
    plot: Plot2D,
    plotId: String = plot.toString(),
    config: PlotlyConfig = PlotlyConfig(),
    container: PlotlyContainer = StaticPlotlyContainer
): Plot2D = with(container) {
    renderPlot(plot, plotId, config)
}

fun FlowContent.plot(
    plotId: String? = null,
    plotlyConfig: PlotlyConfig = PlotlyConfig(),
    container: PlotlyContainer = StaticPlotlyContainer,
    builder: Plot2D.() -> Unit
): Plot2D {
    val plot = Plot2D().apply(builder)
    return plot(plot, plotId ?: plot.toString(), plotlyConfig, container)
}
