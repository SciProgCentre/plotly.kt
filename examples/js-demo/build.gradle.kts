plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
}

dependencies {
    implementation(project(":plotlykt-core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}