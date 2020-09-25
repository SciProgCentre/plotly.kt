plugins {
    id("org.jetbrains.changelog") version "0.4.0"
    kotlin("js") version "1.4.10" apply false
}

val ktorVersion by extra("1.4.0")
val dataforgeVersion by extra("0.2.0-dev-2")
val htmlVersion by extra("0.7.2")

val bintrayRepo by extra("kscience")
val githubProject by extra("plotly.kt")

allprojects {
    group = "kscience.plotlykt"
    version = "0.3.0-dev-3"

    repositories {
        mavenLocal()
    }
}