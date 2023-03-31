plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

kscience{
    jvm()
    js()
    dependencies {
        api(project(":plotlykt-core"))
    }
}

readme{
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}