plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

kscience{
    jvm()
    js()
    native()
    wasm()
    dependencies {
        api(project(":plotlykt-core"))
    }
}

readme{
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}