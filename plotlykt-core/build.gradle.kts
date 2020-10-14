plugins {
    id("ru.mipt.npm.mpp")
    id("ru.mipt.npm.publish")
}

kscience {
    useDokka()
}

val dataforgeVersion: String by rootProject.extra
val htmlVersion: String by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("hep.dataforge:dataforge-meta:$dataforgeVersion")
                api("org.jetbrains.kotlinx:kotlinx-html:$htmlVersion")
                api("org.jetbrains:kotlin-css:1.0.0-pre.122-kotlin-1.4.10")
            }
        }

        jsMain {
            dependencies {
                api(npm("plotly.js", "1.54.6"))
            }
        }
    }
}