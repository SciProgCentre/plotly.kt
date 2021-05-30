plugins {
    kotlin("js") apply false
    kotlin("jupyter.api") apply false
    id("ru.mipt.npm.gradle.project")
}

val dataforgeVersion by extra("0.4.0")

allprojects {
    group = "space.kscience"
    version = "0.4.2-dev-1"

    repositories {
        maven("https://repo.kotlin.link")
        maven("https://kotlin.bintray.com/kotlinx")
    }

    if(name.startsWith("plotlykt")){
        apply<MavenPublishPlugin>()
    }
}

apiValidation {
    ignoredProjects.addAll(listOf("examples", "fx-demo", "js-demo"))
}

ksciencePublish{
    github("plotly.kt")
    space()
    sonatype()
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}

changelog{
    version = project.version.toString()
}