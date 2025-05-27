plugins {
    `maven-publish`
    `java-library`
}

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group as String
                artifactId = project.name

                from(components["java"])
            }
        }

        repositories {
            maven {
                url = uri("https://repo.crazycrew.us/releases/")
                credentials(PasswordCredentials::class)
                authentication.create<BasicAuthentication>("basic")
            }
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}