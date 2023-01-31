plugins {
    kotlin("jvm")
}

repositories {
    maven("https://repo.kotlin.link")
    mavenCentral()
}

dependencies {
    implementation(project(":plotlykt-server"))
    implementation(project(":plotlykt-geo"))
    implementation(kotlin("script-runtime"))
    implementation(project(":plotlykt-script"))
    implementation("org.jetbrains.kotlinx:dataframe:0.8.0-dev-968-0.11.0.83")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs +"-Xopt-in=kotlin.RequiresOptIn"
}

// A workaround for https://youtrack.jetbrains.com/issue/KT-44101

val copyPlotlyResources by tasks.creating(Copy::class){
    dependsOn(":plotlykt-core:jvmProcessResources")
    from(project(":plotlykt-core").buildDir.resolve("processedResources/jvm"))
    into(buildDir.resolve("resources"))
}

tasks.getByName("classes").dependsOn(copyPlotlyResources)

//val runDynamicServer by tasks.creating(JavaExec::class){
//    group = "run"
//    classpath = sourceSets["main"].runtimeClasspath
//    main = "DynamicServerKt"
//}
//
//val runCustomPage by tasks.creating(JavaExec::class){
//    group = "run"
//    classpath = sourceSets["main"].runtimeClasspath
//    main = "CustomPageKt"
//}