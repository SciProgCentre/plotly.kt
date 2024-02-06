import space.kscience.gradle.KScienceVersions

plugins {
    id("space.kscience.gradle.mpp")
    kotlin("jupyter.api")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra
val ktorVersion = KScienceVersions.ktorVersion

kscience{
    jvm()
    commonMain{
        api(projects.plotlyktCore)
        api("io.ktor:ktor-server-cio:$ktorVersion")
        api("io.ktor:ktor-server-html-builder:$ktorVersion")
        api("io.ktor:ktor-server-websockets:$ktorVersion")
        api("io.ktor:ktor-server-cors:$ktorVersion")
        api("space.kscience:dataforge-context:$dataforgeVersion")
    }
}

tasks.processJupyterApiResources{
    libraryProducers = listOf("space.kscience.plotly.server.PlotlyServerIntegration")
}