plugins {
    id("space.kscience.gradle.jvm")
    application
    `maven-publish`
}

kscience{
    application()
}

repositories {
    maven("https://dl.bintray.com/kotlin/ktor/")
}

val ktorVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra

dependencies {
    api(projects.plotlyktCore)
    api(spclibs.kotlinx.html)
    api(kotlin("scripting-jvm-host"))
    api(kotlin("scripting-jvm"))
    api("io.github.microutils:kotlin-logging:3.0.5")
    implementation(spclibs.logback.classic)
    implementation(spclibs.kotlinx.cli)
}

application{
    mainClass.set("space.kscience.plotly.script.CliKt")
}