plugins {
    kotlin("jvm")
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/scientifik")
    maven("https://dl.bintray.com/kotlin/ktor/")
}

dependencies {
    implementation(project(":plotlykt-server"))
}