import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

dependencies {
    implementation(project(":plotlykt-server"))
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}

javafx{
    modules("javafx.web")
    version = "11"
}

application {
    mainClass.set("space.kscience.plotly.fx.PlotlyFXAppKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs +"-Xopt-in=kotlin.RequiresOptIn"
}
