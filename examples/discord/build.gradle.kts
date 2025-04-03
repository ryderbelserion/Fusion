plugins {
    alias(libs.plugins.shadow)

    id("root-plugin")

    application
}

project.group = "${rootProject.group}.discord"
project.version = rootProject.version
project.description = "An example usage of Fusion for Discord Bots!"

repositories {

}

dependencies {
    implementation(project(":fusion-discord"))
}