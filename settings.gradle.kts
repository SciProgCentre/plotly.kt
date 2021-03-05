pluginManagement {
    val kotlinVersion = "1.4.30"
    val toolsVersion = "0.8.4"

    repositories {
        maven("https://repo.kotlin.link")
        jcenter()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlinx")
    }

    plugins {
        id("ru.mipt.npm.gradle.common") version toolsVersion
        id("ru.mipt.npm.gradle.project") version toolsVersion
        id("ru.mipt.npm.gradle.mpp") version toolsVersion
        id("ru.mipt.npm.gradle.jvm") version toolsVersion
        id("ru.mipt.npm.gradle.js") version toolsVersion
        id("ru.mipt.npm.gradle.publish") version toolsVersion
        kotlin("jupyter.api") version "0.8.3.236"
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
    }
}

rootProject.name = "plotlykt"

include(
    ":plotlykt-core",
    ":plotlykt-jupyter",
    ":plotlykt-server",
    ":plotlykt-script",
    ":examples",
    ":fx-demo",
    ":js-demo"
)
