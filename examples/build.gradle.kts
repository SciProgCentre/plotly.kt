plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    jcenter()
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/kscience")
    maven("https://dl.bintray.com/mipt-npm/dev")
}

dependencies {
    implementation(project(":plotlykt-server"))
    implementation(kotlin("script-runtime"))
    implementation(project(":plotlykt-script"))
    implementation("de.mpicbg.scicomp:krangl:0.13")
    implementation("org.apache.commons:commons-csv:1.8")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}
