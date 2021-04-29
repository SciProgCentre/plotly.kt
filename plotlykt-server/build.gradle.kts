plugins {
    id("ru.mipt.npm.gradle.jvm")
    kotlin("jupyter.api")
}

kscience{
    publish()
}

val dataforgeVersion: String by rootProject.extra

dependencies {
    api(project(":plotlykt-core"))
    api("io.ktor:ktor-server-cio:${ru.mipt.npm.gradle.KScienceVersions.ktorVersion}")
    //api("io.ktor:ktor-server-netty:$ktorVersion")
    api("io.ktor:ktor-html-builder:${ru.mipt.npm.gradle.KScienceVersions.ktorVersion}")
    api("io.ktor:ktor-websockets:${ru.mipt.npm.gradle.KScienceVersions.ktorVersion}")
    api("space.kscience:dataforge-context:$dataforgeVersion"){
        exclude(module = "kotlinx-io")
    }
}