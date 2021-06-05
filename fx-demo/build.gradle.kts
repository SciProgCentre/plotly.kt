import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

dependencies {
    implementation(project(":plotlykt-server"))
    implementation("no.tornado:tornadofx:1.7.19")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}

javafx{
    modules("javafx.web")
    version = "11"
}

application {
    mainClass.set("space.kscience.plotly.fx.PlotlyFXAppKt")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}