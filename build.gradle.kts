plugins {
    id("space.kscience.gradle.project")
}

val dataforgeVersion by extra("0.6.0-dev-13")

allprojects {
    group = "space.kscience"
    version = "0.5.3-dev-2"
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