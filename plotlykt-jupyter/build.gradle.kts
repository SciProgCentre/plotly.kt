plugins {
    id("ru.mipt.npm.gradle.jvm")
    kotlin("jupyter.api")
}

kscience{
    publish()
}

val dataforgeVersion: String by rootProject.extra

dependencies {
    api(project(":plotlykt-core"))
}