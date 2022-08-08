plugins {
    id("space.kscience.gradle.jvm")
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