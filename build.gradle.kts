plugins {
    kotlin("jvm") version "1.9.24"
    application
}

group = "com.arblitroshani"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.choco-solver:choco-solver:4.10.18")
}

kotlin {
    jvmToolchain(17)
}
