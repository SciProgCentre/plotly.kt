plugins {
    id("scientifik.mpp") version "0.1.4" apply false
    id("scientifik.jvm") version "0.1.4" apply false
    id("scientifik.publish") version "0.1.4" apply false
}

val ktorVersion by extra("1.2.3")
val dataforgeVersion by extra("0.1.2")
val htmlVersion by extra("0.6.12")

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/mipt-npm/dataforge")
    }

    group = "scientifik"
    version = "0.1.2"
}