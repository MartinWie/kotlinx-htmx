plugins {
    kotlin("jvm") version "2.1.20"
    `maven-publish`
    signing
}

group = "io.github.martinwie"
version = "0.1.0"

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

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.martinwie"
            artifactId = "kotlinx-htmx"

            from(components["java"])

            pom {
                name.set("kotlinx-htmx")
                description.set("HTMX extensions for kotlinx-html - Type-safe HTMX attributes and utilities for Kotlin HTML DSL")
                url.set("https://github.com/MartinWie/kotlinx-htmx")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("MartinWie")
                        name.set("Martin Wiechmann")
                        email.set("donotsuspend@googlegroups.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/MartinWie/kotlinx-htmx.git")
                    developerConnection.set("scm:git:ssh://github.com/MartinWie/kotlinx-htmx.git")
                    url.set("https://github.com/MartinWie/kotlinx-htmx")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("ossrhUsername") as String?
                password = findProperty("ossrhPassword") as String?
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}
