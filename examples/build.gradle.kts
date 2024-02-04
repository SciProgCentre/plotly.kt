plugins {
    kotlin("jvm")
}

repositories {
    maven("https://repo.kotlin.link")
    mavenCentral()
}

dependencies {
    implementation(projects.plotlyktServer)
    implementation(projects.plotlyktJupyter)
    implementation(projects.plotlyktGeo)
    implementation(kotlin("script-runtime"))
    implementation(projects.plotlyktScript)
    implementation("org.jetbrains.kotlinx:dataframe:0.10.1")
}

kotlin{
    jvmToolchain(11)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs +"-Xopt-in=kotlin.RequiresOptIn"
}

// A workaround for https://youtrack.jetbrains.com/issue/KT-44101

val copyPlotlyResources by tasks.creating(Copy::class){
    dependsOn(":plotlykt-core:jvmProcessResources")
    mustRunAfter(":plotlykt-core:jvmTestProcessResources")
    from(project(":plotlykt-core").layout.buildDirectory.file("processedResources/jvm"))
    into(layout.buildDirectory.file("resources"))
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