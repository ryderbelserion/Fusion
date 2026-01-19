plugins {
    id("com.jeff-media.fix-javadoc-plugin")

    `maven-publish`
    `java-library`
}

val libs: VersionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

tasks {
    javadoc {
        val name = rootProject.name
        val options = options as StandardJavadocDocletOptions

        options.encoding = Charsets.UTF_8.name()
        options.overview("src/main/javadoc/overview.html")
        options.use()
        options.isDocFilesSubDirs = true
        options.windowTitle("$name ${project.version} API Documentation")
        options.docTitle("<h1>$name ${project.version} API</h1>")
        options.bottom("Copyright © 2025 Ryder Belserion")
        options.linkSource(true)
        options.addBooleanOption("html5", true)
    }

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
                url = uri(libs.findVersion("url").get().toString())
                credentials(PasswordCredentials::class)
                authentication.create<BasicAuthentication>("basic")
            }
        }
    }

    withType<com.jeff_media.fixjavadoc.FixJavadoc> {
        configureEach {
            newLineOnMethodParameters.set(false)
            keepOriginal.set(false)
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