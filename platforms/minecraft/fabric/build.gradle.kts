plugins {
    alias(libs.plugins.fabric.loom)

    `config-java`
}

dependencies {
    minecraft(libs.minecraft.get())
    mappings(loom.officialMojangMappings())

    modCompileOnly(libs.fabric.loader.get())
    modCompileOnly(libs.fabric.api.get())

    modImplementation(include("net.kyori:adventure-platform-fabric:5.3.0")!!)
    modImplementation(libs.kyori.api)
    modImplementation(libs.kyori.text)

    api(project(":fusion-adventure"))
    shadow(project(":fusion-adventure"))
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations["shadow"])
        exclude("META-INF")

        dependencies {
            include(project(":fusion-adventure"))
            include(project(":fusion-core"))
        }
    }

    remapJar {
        dependsOn(shadowJar)
        mustRunAfter(shadowJar)

        inputFile.set(shadowJar.get().archiveFile)
    }
}