import space.kscience.gradle.useApache2Licence
import space.kscience.gradle.useSPCTeam

plugins {
    id("space.kscience.gradle.project")
}

val dataforgeVersion by extra("0.8.0")
val plotlyVersion by extra("2.29.0")

allprojects {
    group = "space.kscience"
    version = "0.7.0-dev-2"
}

apiValidation {
    ignoredProjects.addAll(listOf("examples", "fx-demo", "js-demo"))
}

ksciencePublish{

    pom("https://github.com/SciProgCentre/plotly.kt") {
        useApache2Licence()
        useSPCTeam()
    }
    repository("spc","https://maven.sciprog.center/kscience")
    sonatype("https://oss.sonatype.org")
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}