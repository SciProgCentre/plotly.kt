plugins {
    id("ru.mipt.npm.gradle.jvm")
    kotlin("jupyter.api")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra

dependencies {
    api(project(":plotlykt-core"))
    api("io.ktor:ktor-server-cio:${ru.mipt.npm.gradle.KScienceVersions.ktorVersion}")
    api("io.ktor:ktor-html-builder:${ru.mipt.npm.gradle.KScienceVersions.ktorVersion}")
    api("io.ktor:ktor-websockets:${ru.mipt.npm.gradle.KScienceVersions.ktorVersion}")
    api("space.kscience:dataforge-context:$dataforgeVersion")
}

tasks.processJupyterApiResources{
    libraryProducers = listOf("space.kscience.plotly.server.PlotlyServerIntegration")
}