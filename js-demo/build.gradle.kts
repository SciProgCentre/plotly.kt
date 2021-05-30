plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
}

dependencies {
    implementation(project(":plotlykt-core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.5.0")
}