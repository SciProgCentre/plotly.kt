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
                api("org.jetbrains:kotlin-css:1.0.0-pre.109-kotlin-1.3.72")
                api("hep.dataforge:dataforge-meta:$dataforgeVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-jvm:$htmlVersion")
                api("org.jetbrains:kotlin-css-jvm:1.0.0-pre.109-kotlin-1.3.72")
            }
        }

        val jsMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-js:${htmlVersion}")
                api("org.jetbrains:kotlin-css-js:1.0.0-pre.109-kotlin-1.3.72")
                api(npm("plotly.js"))
            }
        }
    }
}