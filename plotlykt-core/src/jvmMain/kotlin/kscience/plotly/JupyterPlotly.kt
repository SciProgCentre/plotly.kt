package kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML

@UnstablePlotlyAPI
object JupyterPlotly: PlotlyRenderer {
    override fun FlowContent.renderPlot(plot: Plot, plotId: String, config: PlotlyConfig): Plot {
        div {
            id = plotId
            script {
                unsafe {
                    //language=JavaScript
                    +"""
                        (function (){
                            let theCall = function(){
                                makePlot(
                                    '$plotId',
                                    ${plot.data.toJsonString()},
                                    ${plot.layout.toJsonString()},
                                    $config
                                );                        
                            };
    
                            if(typeof Plotly === 'undefined'){
                                if(!window.plotlyCallQueue) {
                                    window.plotlyCallQueue = [];
                                }                            
                                window.plotlyCallQueue.push(theCall)
                            } else {
                                theCall();
                            }
                        }());
                    """.trimIndent()
                }
            }
        }
        return plot
    }

    fun loadJs() = HtmlFragment {
        script {
            type = "text/javascript"
            unsafe {
                //language=JavaScript
                +"""
                (function() {
                    console.log("Starting up plotly script loader");
                    //initialize LaTeX for Jupyter
                    window.PlotlyConfig = {MathJaxConfig: 'local'};

                    window.startupPlotly = function (){
                        if (window.MathJax){ 
                            MathJax.Hub.Config({
                                SVG: {
                                    font: "STIX-Web"
                                }
                            });
                        }                    
                        console.info("Calling deferred operations in Plotly queue.")
                        if(window.plotlyCallQueue){
                            window.plotlyCallQueue.forEach(function(theCall) {theCall();});
                            window.plotlyCallQueue = [];
                        }
                    }
                })();
                """.trimIndent()
            }
        }

        cdnPlotlyHeader.visit(this)

        script {
            type = "text/javascript"
            val connectorScript = javaClass.getResource("/js/plotlyConnect.js")!!.readText()
            unsafe {
                +connectorScript
            }
            unsafe {
                //language=JavaScript
                +"window.startupPlotly()"
            }
        }
    }

    fun renderPlot(plot: Plot): String = createHTML().div {
        plot(plot, config = PlotlyConfig{
            responsive = true
        }, renderer = this@JupyterPlotly)
    }

    fun renderFragment(fragment: PlotlyFragment): String = createHTML().div {
        with(fragment) {
            render(this@JupyterPlotly)
        }
    }

    fun renderPage(page: PlotlyPage): String = page.copy(renderer = this@JupyterPlotly).render()
}