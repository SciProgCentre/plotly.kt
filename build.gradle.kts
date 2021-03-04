plugins {
    kotlin("js") apply false
    id("ru.mipt.npm.project")
}

val ktorVersion by extra("1.5.0")
val dataforgeVersion by extra("0.4.0-dev-1")

val githubProject by extra("plotly.kt")

allprojects {
    group = "space.kscience"
    version = "0.4.0-dev-1"

    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")
    }
}

apiValidation {
    ignoredProjects.addAll(listOf("examples", "fx-demo", "js-demo"))
}

ksciencePublish{
    spaceRepo = "https://maven.pkg.jetbrains.space/mipt-npm/p/sci/maven"
}