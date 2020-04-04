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
        val jvmTest by getting {
            dependencies {
                val kotest_version = "4.0.2"
                implementation("io.kotest:kotest-runner-junit5-jvm:$kotest_version")
                implementation("io.kotest:kotest-assertions-core-jvm:$kotest_version")
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

tasks.withType<Test> {
    useJUnitPlatform()
}
