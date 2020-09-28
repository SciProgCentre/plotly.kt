plugins {
    id("ru.mipt.npm.mpp")
}

kscience {
    useDokka()
    publish()
}

val dataforgeVersion: String by rootProject.extra
val htmlVersion: String by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("hep.dataforge:dataforge-meta:$dataforgeVersion")
//                api("hep.dataforge:dataforge-meta-metadata:$dataforgeVersion")

                api("org.jetbrains.kotlinx:kotlinx-html:$htmlVersion")
                api("org.jetbrains:kotlin-css:1.0.0-pre.112-kotlin-1.4.0")
            }
        }

        jvmMain {
            dependencies {
                api("hep.dataforge:dataforge-meta-jvm:$dataforgeVersion")
            }
        }

        jsMain {
            dependencies {
                api(npm("plotly.js", "1.54.6"))
            }
        }
    }
}