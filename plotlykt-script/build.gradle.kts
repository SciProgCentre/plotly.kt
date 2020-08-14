plugins {
    id("scientifik.jvm")
    id("scientifik.publish")
    id("org.jetbrains.dokka")
    application
}

repositories {
    maven("https://dl.bintray.com/kotlin/ktor/")
}

val ktorVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra

dependencies {
    api(project(":plotlykt-core"))
    api(kotlin("scripting-jvm-host"))
    api(kotlin("scripting-jvm"))
    api("io.github.microutils:kotlin-logging:1.8.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.2.1")
}

application{
    mainClassName = "kscience.plotly.script.CliKt"
}