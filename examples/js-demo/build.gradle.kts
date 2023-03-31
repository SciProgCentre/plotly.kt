plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
}

dependencies {
    implementation(projects.plotlyktCore)
    implementation(spclibs.kotlinx.coroutines.core)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs +"-Xopt-in=kotlin.RequiresOptIn"
}