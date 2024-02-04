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
    wasm()

    dependencies {
        api("space.kscience:dataforge-meta:$dataforgeVersion")
        //api(spclibs.kotlinx.html)
        api("org.jetbrains.kotlinx:kotlinx-html:0.11.0")
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