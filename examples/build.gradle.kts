plugins {
    kotlin("jvm")
}

repositories{
    maven("https://dl.bintray.com/kotlin/ktor/")
}

dependencies {
    implementation(project(":plotlykt-server"))
}