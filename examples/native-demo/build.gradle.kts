import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

kotlin {
    linuxX64{
        binaries{
            executable()
        }
    }

    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":plotlykt-core"))
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs +"-Xopt-in=kotlin.RequiresOptIn"
}
