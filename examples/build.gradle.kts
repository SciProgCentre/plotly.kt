plugins {
    id("scientifik.jvm")
}

dependencies {
    implementation(project(":plotlykt-server"))
    implementation(kotlin("script-runtime"))
}
