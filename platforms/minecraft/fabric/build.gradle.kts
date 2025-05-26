plugins {
    alias(libs.plugins.fabric.loom)

    `config-java`
}

dependencies {
    minecraft(libs.minecraft.get())
    mappings(loom.officialMojangMappings())

    modCompileOnly(libs.fabric.loader.get())
    modCompileOnly(libs.fabric.api.get())

    api(project(":fusion-adventure"))
}