plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

kscience{
    jvm()
    js()
    native()
    dependencies {
        api(project(":plotlykt-core"))
    }
}

readme{
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}