plugins {
    id("scientifik.jvm")
}

dependencies {
    implementation(project(":plotlykt-server"))
    implementation(kotlin("script-runtime"))
    implementation("de.mpicbg.scicomp:krangl:0.13")
    implementation("org.apache.commons:commons-csv:1.8")
}
