plugins {
    id("fusion.parent")

    `maven-publish`
}

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withSourcesJar()
    withJavadocJar()
}

tasks {
    publishing {
        repositories {
            maven {
                url = uri("https://repo.ryderbelserion.com/releases")

                credentials {
                    this.username = System.getenv("gradle_username")
                    this.password = System.getenv("gradle_password")
                }
            }
        }
    }

    compileJava {
        options.encoding = "UTf-8"
        options.compilerArgs.add("-parameters")
        options.release.set(21)
    }

    javadoc {
        options.encoding = "UTf-8"
    }

    processResources {
        filteringCharset = "UTf-8"
    }
}