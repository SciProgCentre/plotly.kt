plugins {
    id("space.kscience.gradle.mpp")
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
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}