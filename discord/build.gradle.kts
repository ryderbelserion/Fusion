plugins {
    id("root-plugin")

    alias(libs.plugins.shadow)
}

project.group = "${rootProject.group}.discord"
project.version = rootProject.version
project.description = "A version of Fusion for Discord bots!"

repositories {

}

dependencies {
    api(project(":fusion-core"))

    // Adventure
    api(libs.bundles.adventure)

    // Configurate
    api(libs.configurate.yaml)

    // JDA
    api(libs.logback)
    api(libs.jda)
}