import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
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
