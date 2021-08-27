plugins {
    id("ru.mipt.npm.gradle.mpp")
    `maven-publish`
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":plotlykt-core"))
            }
        }
    }
}

readme{
    maturity = ru.mipt.npm.gradle.Maturity.EXPERIMENTAL
}