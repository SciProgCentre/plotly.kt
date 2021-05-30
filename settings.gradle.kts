pluginManagement {
    val kotlinVersion = "1.5.10"
    val toolsVersion = "0.9.9"

    repositories {
        maven("https://repo.kotlin.link")
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("ru.mipt.npm.gradle.common") version toolsVersion
        id("ru.mipt.npm.gradle.project") version toolsVersion
        id("ru.mipt.npm.gradle.mpp") version toolsVersion
        id("ru.mipt.npm.gradle.jvm") version toolsVersion
        id("ru.mipt.npm.gradle.js") version toolsVersion
        id("ru.mipt.npm.gradle.publish") version toolsVersion
        kotlin("jupyter.api") version "0.10.0-25"
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
