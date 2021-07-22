rootProject.name = "plotly-kt"

pluginManagement {
    val toolsVersion = "0.10.0"

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
    }
}

include(
    ":plotlykt-core",
    ":plotlykt-jupyter",
    ":plotlykt-server",
    ":plotlykt-script",
    ":examples",
    ":fx-demo",
    ":js-demo"
)
