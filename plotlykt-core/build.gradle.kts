plugins {
    id("ru.mipt.npm.gradle.mpp")
}

kscience {
    publish()
}

val dataforgeVersion: String by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("space.kscience:dataforge-meta:$dataforgeVersion")
                api("org.jetbrains.kotlinx:kotlinx-html:${ru.mipt.npm.gradle.KScienceVersions.htmlVersion}")
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