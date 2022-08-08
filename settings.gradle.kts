rootProject.name = "plotly-kt"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {

    val toolsVersion: String by extra

    repositories {
        mavenLocal()
        maven("https://repo.kotlin.link")
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("space.kscience.gradle.project") version toolsVersion
        id("space.kscience.gradle.mpp") version toolsVersion
        id("space.kscience.gradle.jvm") version toolsVersion
        id("space.kscience.gradle.js") version toolsVersion
    }
}

dependencyResolutionManagement {

    val toolsVersion: String by extra

    repositories {
        mavenLocal()
        maven("https://repo.kotlin.link")
        mavenCentral()
    }

    versionCatalogs {
        create("npmlibs") {
            from("space.kscience:version-catalog:$toolsVersion")
        }
    }
}

include(
    ":plotlykt-core",
    ":plotlykt-geo",
    ":plotlykt-jupyter",
    ":plotlykt-server",
    ":plotlykt-script",
    ":examples",
    ":examples:fx-demo",
    ":examples:js-demo"
)