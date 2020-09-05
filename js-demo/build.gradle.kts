plugins {
    kotlin("js")
}

repositories {
    mavenLocal()
    jcenter()
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/kscience")
    maven("https://dl.bintray.com/mipt-npm/dev")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
}

dependencies {
    implementation(project(":plotlykt-core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.9")
}