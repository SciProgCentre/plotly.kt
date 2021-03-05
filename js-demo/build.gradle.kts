plugins {
    kotlin("js")
}

repositories {
    mavenLocal()
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.2")
}