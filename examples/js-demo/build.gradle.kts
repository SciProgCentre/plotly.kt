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
    implementation(project(":plotlykt-core"))
    implementation(npmlibs.kotlinx.coroutines.core)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs +"-Xopt-in=kotlin.RequiresOptIn"
}