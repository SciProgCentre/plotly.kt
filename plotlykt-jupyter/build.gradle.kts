plugins {
    id("ru.mipt.npm.gradle.jvm")
    kotlin("jupyter.api")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra

dependencies {
    api(project(":plotlykt-core"))
}

tasks.processJupyterApiResources{
    libraryProducers = listOf("space.kscience.plotly.PlotlyIntegration")
}