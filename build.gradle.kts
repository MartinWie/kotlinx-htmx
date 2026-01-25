plugins {
    kotlin("jvm") version "2.2.0"
    id("com.vanniktech.maven.publish") version "0.36.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // Core dependency for kotlinx-html DSL
    api("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")

    // JetBrains annotations for @Language hints in IDE
    compileOnly("org.jetbrains:annotations:26.0.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
