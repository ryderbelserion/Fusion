plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.core"

repositories {
    maven("https://libraries.minecraft.net/")
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    compileOnly(libs.brigadier)

    compileOnlyApi(libs.jalu)
}