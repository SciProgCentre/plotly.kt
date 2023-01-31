import space.kscience.gradle.isInDevelopment
import space.kscience.gradle.useApache2Licence
import space.kscience.gradle.useSPCTeam

plugins {
    id("space.kscience.gradle.project")
}

val dataforgeVersion by extra("0.6.0-dev-15")

allprojects {
    group = "space.kscience"
    version = "0.5.3-dev-2"
}

apiValidation {
    ignoredProjects.addAll(listOf("examples", "fx-demo", "js-demo"))
}

ksciencePublish{

    pom("https://github.com/SciProgCentre/plotly.kt") {
        useApache2Licence()
        useSPCTeam()
    }
    github("plotly.kt", "SciProgCentre")
    space(
        if (isInDevelopment) {
            "https://maven.pkg.jetbrains.space/spc/p/sci/dev"
        } else {
            "https://maven.pkg.jetbrains.space/spc/p/sci/release"
        }
    )
    sonatype()
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}