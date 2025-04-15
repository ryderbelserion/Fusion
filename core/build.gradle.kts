plugins {
    id("root-plugin")

    alias(libs.plugins.shadow)
}

project.group = "${rootProject.group}.core"
project.version = rootProject.version

base {
    archivesName.set("fusion-core")
}

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    compileOnly(libs.brigadier)

    compileOnly(libs.jetbrains)

    //api(libs.configurate.jackson)

    api(libs.jalu)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
}