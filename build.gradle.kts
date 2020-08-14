plugins {
    val toolsVersion = "0.5.2"
    id("scientifik.mpp") version toolsVersion apply false
    id("scientifik.jvm") version toolsVersion apply false
    id("scientifik.publish") version toolsVersion apply false
}

val ktorVersion by extra("1.3.2")
val dataforgeVersion by extra("0.1.8")
val htmlVersion by extra("0.7.1")

val bintrayRepo by extra("scientifik")
val githubProject by extra("plotly.kt")

allprojects {
    group = "kscience"
    version = "0.2.0"
}