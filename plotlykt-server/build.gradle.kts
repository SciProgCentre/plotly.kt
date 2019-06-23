import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

repositories {
    maven("https://dl.bintray.com/kotlin/ktor/")
}

val ktorVersion = Versions.ktorVersion

dependencies {
    api(project(":plotlykt-core"))
    api("io.ktor:ktor-server-cio:$ktorVersion")
    api("io.ktor:ktor-html-builder:$ktorVersion")
    api("io.ktor:ktor-websockets:$ktorVersion")
    api("hep.dataforge:dataforge-output-jvm:${Versions.dataforgeVersion}")
}

publishing {
    publications {
        create<MavenPublication>("jvm") {
            from(components["java"])
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
