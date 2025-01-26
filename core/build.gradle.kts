plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.core"
project.version = rootProject.version
project.description = "A platform independent version of Fusion!"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    compileOnly(libs.brigadier)

    api(libs.configurate.jackson)
}