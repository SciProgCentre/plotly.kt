plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/scientifik")
    maven("https://dl.bintray.com/kotlin/ktor/")
}

dependencies {
    // use implementation("scientifik:plotlykt-server:0.1.1") for external project
    implementation(project(":plotlykt-server"))
}