package space.kscience.plotly.script

import kotlinx.html.FlowContent
import mu.KLogger
import mu.KotlinLogging
import space.kscience.plotly.*
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.defaultJvmScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

@UnstablePlotlyAPI
public fun Plotly.page(
    source: SourceCode,
    title: String = "Plotly.kt",
    headers: Array<PlotlyHtmlFragment> = arrayOf(cdnPlotlyHeader),
    logger: KLogger = KotlinLogging.logger("scripting")
): PlotlyPage {

    val workspaceScriptConfiguration = ScriptCompilationConfiguration {
        baseClass(PlotlyScript::class)
        implicitReceivers(FlowContent::class)
        defaultImports(
            "kotlin.math.*",
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*"
        )
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
            compilerOptions.append("-jvm-target", Runtime.version().feature().toString())
        }
        hostConfiguration(defaultJvmScriptingHostConfiguration)
    }

    return page(title = title, headers = headers) {
        val flow = this
        val evaluationConfiguration = ScriptEvaluationConfiguration {
            implicitReceivers(flow)
        }
        BasicJvmScriptingHost().eval(source, workspaceScriptConfiguration, evaluationConfiguration).onFailure {
            it.reports.forEach { scriptDiagnostic ->
                when (scriptDiagnostic.severity) {
                    ScriptDiagnostic.Severity.FATAL, ScriptDiagnostic.Severity.ERROR -> {
                        logger.error(scriptDiagnostic.exception) { scriptDiagnostic.toString() }
                        error(scriptDiagnostic.toString())
                    }
                    ScriptDiagnostic.Severity.WARNING -> logger.warn { scriptDiagnostic.toString() }
                    ScriptDiagnostic.Severity.INFO -> logger.info { scriptDiagnostic.toString() }
                    ScriptDiagnostic.Severity.DEBUG -> logger.debug { scriptDiagnostic.toString() }
                }
            }
        }
    }
}

@UnstablePlotlyAPI
public fun Plotly.page(
    file: File,
    title: String = "Plotly.kt",
    headers: Array<PlotlyHtmlFragment> = arrayOf(cdnPlotlyHeader),
    logger: KLogger = KotlinLogging.logger("scripting")
): PlotlyPage = page(file.toScriptSource(), title, headers, logger)


@OptIn(UnstablePlotlyAPI::class)
public fun Plotly.page(
    string: String,
    title: String = "Plotly.kt",
    headers: Array<PlotlyHtmlFragment> = arrayOf(cdnPlotlyHeader),
    logger: KLogger = KotlinLogging.logger("scripting")
): PlotlyPage = page(string.toScriptSource(), title, headers, logger)