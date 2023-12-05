plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra
val plotlyVersion: String by rootProject.extra


kotlin{
    applyDefaultHierarchyTemplate()
}

kscience {
    fullStack(bundleName = "js/plotly-kt.js")
    native()

    dependencies {
        api("space.kscience:dataforge-meta:$dataforgeVersion")
        api(spclibs.kotlinx.html)
    }

    jsMain{
        api(npm("plotly.js", plotlyVersion))
    }

    nativeMain {
        implementation("com.squareup.okio:okio:3.3.0")
    }
}


readme {
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}