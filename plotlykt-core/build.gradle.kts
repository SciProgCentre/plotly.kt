plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra
val plotlyVersion: String by rootProject.extra

kscience {
    jvm()
    js()
    dependencies {
        api("space.kscience:dataforge-meta:$dataforgeVersion")
        api(spclibs.kotlinx.html)
    }
    dependencies(jsMain) {
        api(npm("plotly.js", plotlyVersion))
    }
}

readme {
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}