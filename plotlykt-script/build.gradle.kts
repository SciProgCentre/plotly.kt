plugins {
    id("space.kscience.gradle.jvm")
    application
    `maven-publish`
}

kscience{
    useHtml()
    application()
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
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3")
}

application{
    mainClass.set("space.kscience.plotly.script.CliKt")
}