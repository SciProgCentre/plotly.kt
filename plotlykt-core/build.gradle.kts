plugins {
    id("ru.mipt.npm.gradle.mpp")
    `maven-publish`
}

kscience {
    useHtml()
}

val dataforgeVersion: String by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("space.kscience:dataforge-meta:$dataforgeVersion")
            }
        }

        jsMain {
            dependencies {
                api(npm("plotly.js", "1.54.6"))
            }
        }
    }
}