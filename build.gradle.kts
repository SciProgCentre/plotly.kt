plugins {
    id("org.jetbrains.changelog") version "0.4.0"
    kotlin("js") version "1.4.0" apply false
}

val ktorVersion by extra("1.4.0")
val dataforgeVersion by extra("0.1.9-dev-5")
val htmlVersion by extra("0.7.2")

val bintrayRepo by extra("kscience")
val githubProject by extra("plotly.kt")

allprojects {
    group = "kscience.plotlykt"
    version = "0.3.0-dev-2"

    repositories {
        mavenLocal()
    }
}