plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.name}.kyori"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    api(project(":fusion-core"))

    compileOnly(libs.bundles.adventure)
    compileOnly(libs.brigadier)
}