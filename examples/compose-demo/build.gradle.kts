import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.5.12"
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
    maven("https://jogamp.org/deployment/maven")
}

kotlin{
    jvm()
    jvmToolchain(17)
    sourceSets{
        jvmMain{
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.desktop.currentOs)
                implementation("io.github.kevinnzou:compose-webview-multiplatform:1.8.6")
                implementation(projects.plotlyktServer)
                implementation(spclibs.logback.classic)
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

compose {
    desktop {
        application {
            mainClass = "space.kscience.plotly.compose.AppKt"
        }
    }
}
