plugins {
    val toolsVersion = "0.4.2"
    id("scientifik.mpp") version toolsVersion apply false
    id("scientifik.jvm") version toolsVersion apply false
    id("scientifik.publish") version toolsVersion apply false
}

val ktorVersion by extra("1.3.2")
val dataforgeVersion by extra("0.1.7")
val htmlVersion by extra("0.7.1")

val bintrayRepo by extra("scientifik")
val githubProject by extra("plotly.kt")

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/mipt-npm/dev")
        maven("https://dl.bintray.com/mipt-npm/scientifik")
        maven("https://dl.bintray.com/mipt-npm/dataforge")
    }

    group = "scientifik"
    version = "0.2.0-dev-1"
}