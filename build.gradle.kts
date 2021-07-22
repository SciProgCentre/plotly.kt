plugins {
    id("ru.mipt.npm.gradle.project")
}

val dataforgeVersion by extra("0.5.0-dev-2")

allprojects {
    group = "space.kscience"
    version = "0.4.5"
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