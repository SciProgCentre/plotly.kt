plugins {
    id("ru.mipt.npm.jvm")
    id("ru.mipt.npm.publish")
}

kscience{
    useDokka()
}

//repositories {
//    maven("https://dl.bintray.com/kotlin/ktor/")
//}

val ktorVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra

dependencies {
    api(project(":plotlykt-core"))
    api("io.ktor:ktor-server-cio:$ktorVersion")
    //api("io.ktor:ktor-server-netty:$ktorVersion")
    api("io.ktor:ktor-html-builder:$ktorVersion")
    api("io.ktor:ktor-websockets:$ktorVersion")
    api("hep.dataforge:dataforge-output:$dataforgeVersion"){
        exclude(module = "kotlinx-io")
    }
}