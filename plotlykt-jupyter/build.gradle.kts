plugins {
    id("space.kscience.gradle.mpp")
    kotlin("jupyter.api")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra

kscience{
    jvm()
    jvmMain{
        api(projects.plotlyktCore)
    }
}

tasks.processJupyterApiResources{
    libraryProducers = listOf("space.kscience.plotly.PlotlyIntegration")
}