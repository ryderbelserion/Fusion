plugins {
    alias(libs.plugins.fabric.loom)

    `config-java`
}

dependencies {
    implementation(project(":fusion-fabric"))

    minecraft(libs.minecraft.get())
    mappings(loom.officialMojangMappings())

    modCompileOnly(libs.fabric.loader.get())
    modCompileOnly(libs.fabric.api.get())
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
}