plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.common"

repositories {
    maven("https://libraries.minecraft.net/")
}

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.configurate.json)
    compileOnly(libs.configurate.yaml)
    compileOnly(libs.brigadier)
}