plugins {
     kotlin("js")
}

repositories {
     mavenLocal()
     jcenter()
     maven("https://dl.bintray.com/mipt-npm/dataforge")
     maven("https://dl.bintray.com/mipt-npm/scientifik")
     maven("https://dl.bintray.com/mipt-npm/dev")
     maven("https://dl.bintray.com/kotlin/ktor/")
     maven("https://dl.bintray.com/kotlin/kotlin-wrappers")
     maven("https://kotlin.bintray.com/kotlinx")
}

kotlin{
     target {
          browser()
     }
}

dependencies{
     implementation(project(":plotlykt-core"))
     implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.7")
}