plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.core"
project.version = rootProject.version

repositories {
    maven("https://libraries.minecraft.net")
}


dependencies {
    compileOnly(libs.configurate.yaml)

    compileOnly(libs.bundles.adventure)

    compileOnly(libs.brigadier)

    compileOnly(libs.jetbrains)

    api(libs.jalu)
}