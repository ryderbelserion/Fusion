plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.core"
project.version = rootProject.version

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.brigadier)

    api(project(":fusion-api"))
}