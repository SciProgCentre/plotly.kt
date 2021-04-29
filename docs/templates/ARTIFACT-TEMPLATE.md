> #### Artifact:
>
> This module artifact: `${group}:${name}:${version}`.
>
>
> [![Maven Central](https://img.shields.io/maven-central/v/space.kscience/${name}.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22space.kscience%22%20AND%20a:%22${name}%22)
>
> **Gradle:**
>
> ```gradle
> repositories {
>     maven { url 'https://repo.kotlin.link' }
> }
> 
> dependencies {
>     implementation '${group}:${name}:${version}'
> }
> ```
> **Gradle Kotlin DSL:**
>
> ```kotlin
> repositories {
>     maven("https://https://repo.kotlin.link")
> }
> 
> dependencies {
>     implementation("${group}:${name}:${version}")
> }
> ```