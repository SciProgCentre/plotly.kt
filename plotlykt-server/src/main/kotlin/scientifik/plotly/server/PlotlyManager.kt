package scientifik.plotly.server

//
//class PlotlyManager(meta: Meta) : AbstractPlugin(meta), OutputManager {
//    override val tag: PluginTag get() = PlotlyManager.tag
//
//    private val cells = HashMap<Name, PlotOutput>()
//
//    private val server = embeddedServer(CIO, 7777) {
//        routing {
//            get("/") {
//                val stage = call.request.queryParameters["stage"]?.toName() ?: EmptyName
//                val name = call.request.queryParameters["name"]?.toName() ?: EmptyName
//                if( name.isEmpty()) {
//                    call.respondHtml {
//                        body {
//                            h1 { +"This is plotly server root" }
//                        }
//                    }
//                } else {
//                    call.r
//                }
//            }
//        }
//    }
//
//    override fun attach(context: Context) {
//        super.attach(context)
//        server.start()
//    }
//
//    override fun detach() {
//        super.detach()
//        server.stop(1000, 5000, TimeUnit.MILLISECONDS)
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : Any> get(type: KClass<out T>, name: Name, stage: Name, meta: Meta): Output<T> {
//        return when (type) {
//            Trace::class -> cells.getOrPut(stage + name) { PlotOutput(Layout(meta.toConfig())) }
//            else -> error("Intput type $type not recognized")
//        } as Output<T>
//    }
//
//    inner class PlotOutput(layout: Layout) : Output<Trace> {
//        override val context: Context get() = this@PlotlyManager.context
//
//        val plot: Plot2D = Plot2D.empty().apply { this.layout = layout }
//
//        override fun render(obj: Trace, meta: Meta) {
//            plot.trace(obj)
//        }
//
//    }
//
//    companion object : PluginFactory<PlotlyManager> {
//        override val tag = PluginTag("output.console", group = PluginTag.DATAFORGE_GROUP)
//
//        override val type = PlotlyManager::class
//
//        override fun invoke(meta: Meta) = PlotlyManager(meta)
//    }
//
//}
