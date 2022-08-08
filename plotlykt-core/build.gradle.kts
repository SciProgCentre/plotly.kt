plugins {
    id("space.kscience.gradle.mpp")
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

readme{
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}

rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}