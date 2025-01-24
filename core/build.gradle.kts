plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.core"
project.version = rootProject.version
project.description = "A platform independent version of Fusion!"

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    api(libs.configurate.jackson)
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    val sourcesJar by registering(Jar::class, fun Jar.() {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    })

    val javadocJar by registering(Jar::class, fun Jar.() {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    })

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group as String
                artifactId = project.name

                from(javaComponent)

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }

                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }

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

                    artifact(sourcesJar)
                    artifact(javadocJar)
                }
            }
        }
    }

    signing {
        sign(publishing.publications["maven"])
    }
}