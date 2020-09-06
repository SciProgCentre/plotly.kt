plugins {
    id("scientifik.mpp")
    id("scientifik.publish")
    id("org.jetbrains.dokka")
}


val dataforgeVersion: String by rootProject.extra
val htmlVersion: String by rootProject.extra

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-common:$htmlVersion")
                implementation("org.jetbrains:kotlin-css:1.0.0-pre.109-kotlin-1.3.72")
                api("hep.dataforge:dataforge-meta") {
                    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
                    version {
                        prefer(dataforgeVersion)
                    }
                }
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common") {
                    version {
                        strictly("0.20.0")
                    }
                }
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-jvm:$htmlVersion")
                implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.109-kotlin-1.3.72")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime") {
                    version {
                        strictly("0.20.0")
                    }
                }
            }
        }

        val jsMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-js:${htmlVersion}")
                implementation("org.jetbrains:kotlin-css-js:1.0.0-pre.109-kotlin-1.3.72")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js") {
                    version {
                        strictly("0.20.0")
                    }
                }
                implementation(npm("plotly.js", "1.54.6"))
            }
        }
    }
}