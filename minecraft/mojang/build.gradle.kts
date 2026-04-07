plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.mojang"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(project(":fusion-kyori"))

    compileOnly(libs.bundles.adventure)
    compileOnly(libs.brigadier)
    compileOnly(libs.jetbrains)
}