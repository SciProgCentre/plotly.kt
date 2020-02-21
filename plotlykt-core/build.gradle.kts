plugins {
    id("scientifik.mpp")
    id("scientifik.publish")
}

val dataforgeVersion: String by rootProject.extra
val htmlVersion: String by rootProject.extra

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-common:$htmlVersion")
                api("hep.dataforge:dataforge-io:$dataforgeVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-jvm:$htmlVersion")
            }
        }

        val jsMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-js:${htmlVersion}")
                api(npm("text-encoding")) //FIX for https://github.com/Kotlin/kotlinx-io/issues/57
            }
        }
    }
}