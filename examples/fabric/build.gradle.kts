plugins {
    alias(libs.plugins.fabric.loom)

    `config-java`
}

dependencies {
    minecraft(libs.minecraft.get())
    mappings(loom.officialMojangMappings())

    modCompileOnly(libs.fabric.loader.get())
    modCompileOnly(libs.fabric.api.get())

    api(project(":fusion-fabric"))
    shadow(project(":fusion-fabric"))
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        with(copySpec {
            from("src/main/resources/fabric.mod.json") {
                expand(
                    "minecraft" to libs.versions.minecraft.get(),
                    "fabricloader" to libs.versions.fabric.loader.get()
                )
            }
        })
    }

    shadowJar {
        configurations = listOf(project.configurations["shadow"])
        exclude("META-INF")

        dependencies {
            include(project(":fusion-fabric"))
        }
    }

    remapJar {
        dependsOn(shadowJar)
        mustRunAfter(shadowJar)

        inputFile.set(shadowJar.get().archiveFile)
    }

    build {
        doLast {
            copy {
                from(remapJar.get().archiveFile)
                into(project.projectDir.resolve("run").resolve("mods"))
            }
        }
    }
}