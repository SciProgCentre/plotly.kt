plugins {
    id("ru.mipt.npm.gradle.jvm")
    kotlin("jupyter.api")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra
val ktorVersion = "1.6.6"

dependencies {
    api(project(":plotlykt-core"))
    api("io.ktor:ktor-server-cio:$ktorVersion")
    api("io.ktor:ktor-html-builder:$ktorVersion")
    api("io.ktor:ktor-websockets:$ktorVersion")
    api("space.kscience:dataforge-context:$dataforgeVersion")
}

tasks.processJupyterApiResources{
    libraryProducers = listOf("space.kscience.plotly.server.PlotlyServerIntegration")
}