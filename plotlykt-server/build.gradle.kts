plugins {
    id("scientifik.jvm")
    id("scientifik.publish")
}

repositories {
    maven("https://dl.bintray.com/kotlin/ktor/")
}

val ktorVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra

scientifik{
    withDokka()
}

dependencies {
    api(project(":plotlykt-core"))
    api("io.ktor:ktor-server-cio:$ktorVersion")
    api("io.ktor:ktor-html-builder:$ktorVersion")
    api("io.ktor:ktor-websockets:$ktorVersion")
    api("hep.dataforge:dataforge-output-jvm:$dataforgeVersion")
}
