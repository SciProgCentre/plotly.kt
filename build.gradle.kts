allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/mipt-npm/dataforge")
        //maven("http://npm.mipt.ru:8081/artifactory/gradle-dev")
    }

    group = "scientifik"
    version = "0.1.1"
}

subprojects {
    apply(plugin = "dokka-publish")
    if (name.startsWith("plotlykt")) {
        apply(plugin = "npm-publish")
    }
}