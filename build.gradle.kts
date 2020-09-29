plugins {
    kotlin("js") apply false
    id("ru.mipt.npm.project")
}

val ktorVersion by extra("1.4.0")
val dataforgeVersion by extra("0.2.0-dev-3")
val htmlVersion by extra("0.7.2")

val bintrayRepo by extra("kscience")
val githubProject by extra("plotly.kt")

allprojects {
    group = "kscience.plotlykt"
    version = "0.3.0-dev-3"

    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")
    }
}