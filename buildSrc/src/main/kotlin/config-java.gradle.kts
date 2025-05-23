plugins {
    id("com.gradleup.shadow")

    `java-library`

    `maven-publish`
    signing
}

project.version = rootProject.version

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.codemc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/libraries")
    maven("https://repo.crazycrew.us/releases")

    maven("https://jitpack.io")

    mavenCentral()
}

dependencies {
    compileOnly(libs.findLibrary("annotations").get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withSourcesJar()
    withJavadocJar()
}

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group as String
                artifactId = project.name

                from(components["java"])

                pom {
                    name.set(rootProject.name)

                    description.set(rootProject.description)

                    url.set("https://github.com/ryderbelserion/Fusion")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                            distribution.set("https://github.com/ryderbelserion/Fusion")
                        }
                    }

                    developers {
                        developer {
                            id.set("ryderbelserion")
                            name.set("Ryder Belserion")
                            email.set("contact@ryderbelserion.com")
                            url.set("https://github.com/ryderbelserion")
                            timezone.set("America/New_York")
                        }
                    }

                    scm {
                        url.set("https://github.com/ryderbelserion/Fusion")
                        connection.set("scm:git:git://github.com/ryderbelserion/Fusion.git")
                        developerConnection.set("scm:git:ssh://git@github.com/ryderbelserion/Fusion.git")
                    }
                }
            }
        }

        repositories {
            maven {
                url = uri("https://repo.crazycrew.us/releases")

                credentials {
                    this.username = System.getenv("gradle_username")
                    this.password = System.getenv("gradle_password")
                }
            }
        }
    }

    signing {
        sign(publishing.publications["maven"])
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}