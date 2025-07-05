plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.core"

repositories {
    maven("https://libraries.minecraft.net/")
}

dependencies {
    compileOnlyApi(libs.bundles.adventure)
    compileOnlyApi(libs.configurate.json)
    compileOnlyApi(libs.configurate.yaml)
    compileOnlyApi(libs.brigadier)
}