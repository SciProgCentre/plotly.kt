plugins {
    kotlin("js") apply false
    kotlin("jupyter.api") apply false
    id("ru.mipt.npm.gradle.project")
}

val dataforgeVersion by extra("0.4.0-dev-1")

val githubProject by extra("plotly.kt")

allprojects {
    group = "space.kscience"
    version = "0.4.0-dev-1"

    repositories {
        mavenLocal()
        maven("https://repo.kotlin.link")
        maven("https://kotlin.bintray.com/kotlinx")
    }
}

apiValidation {
    ignoredProjects.addAll(listOf("examples", "fx-demo", "js-demo"))
}

ksciencePublish{
    spaceRepo = "https://maven.pkg.jetbrains.space/mipt-npm/p/sci/maven"
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}