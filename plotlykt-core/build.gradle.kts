plugins {
    `npm-multiplatform`
}

kotlin {
    jvm()
    js()
    sourceSets {
        val commonMain by getting{
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-common:${Versions.htmlVersion}")
                api("hep.dataforge:dataforge-io:${Versions.dataforgeVersion}")
                //api("hep.dataforge:dataforge-io-metadata:${Versions.dataforgeVersion}")
            }
        }

        val jvmMain by getting{
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.htmlVersion}")
                //api("hep.dataforge:dataforge-io-jvm:${Versions.dataforgeVersion}")
            }
        }

        val jsMain by getting{
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-html-js:${Versions.htmlVersion}")
                //api("hep.dataforge:dataforge-io-js:${Versions.dataforgeVersion}")
            }
        }
    }
}