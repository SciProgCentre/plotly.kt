package space.kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.*
import space.kscience.plotly.Plotly.PLOTLY_CDN

@UnstablePlotlyAPI
@JupyterLibrary
internal class PlotlyIntegration : JupyterIntegration(), PlotlyRenderer {

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

    private fun loadJs(): PlotlyHtmlFragment = PlotlyHtmlFragment {
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

    private fun renderPlot(plot: Plot): String = createHTML().div {
        plot(plot, config = PlotlyConfig {
            responsive = true
        }, renderer = this@PlotlyIntegration)
    }

    private fun renderFragment(fragment: PlotlyFragment): String = createHTML().div {
        with(fragment) {
            render(this@PlotlyIntegration)
        }
    }

    private fun renderPage(page: PlotlyPage): String = page.copy(renderer = this@PlotlyIntegration).render()


    private val plotlyBundle = ResourceFallbacksBundle(listOf(
        ResourceLocation(
            PLOTLY_CDN,
            ResourcePathType.URL
        ),
        ResourceLocation(
            "js/plotly.min.js",
            ResourcePathType.CLASSPATH_PATH
        )
    ))

    private val plotlyResource =
        LibraryResource(name = "plotly", type = ResourceType.JS, bundles = listOf(plotlyBundle))

    private val plotlyConnectBundle = ResourceFallbacksBundle(listOf(
        ResourceLocation(
            "js/plotlyConnect.js",
            ResourcePathType.CLASSPATH_PATH
        )
    ))

    private val plotlyConnectResource =
        LibraryResource(name = "plotlyConnect", type = ResourceType.JS, bundles = listOf(plotlyConnectBundle))


    override fun Builder.onLoaded() {

        resource(plotlyResource)
        resource(plotlyConnectResource)

        repositories("https://repo.kotlin.link")

        import(
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*"
        )

        onLoaded {
            display(HTML(loadJs().toString()))
        }

        render<PlotlyHtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> {
            HTML(renderPlot(it))
        }

        render<PlotlyFragment> {
            HTML(renderFragment(it))
        }

        render<PlotlyPage> {
            HTML(renderPage(it), true)
        }
    }

}
