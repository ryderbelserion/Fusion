plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.commands"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.brigadier)
}