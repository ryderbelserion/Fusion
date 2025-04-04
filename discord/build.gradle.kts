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
    api(project(":fusion-api"))

    api(libs.logback)
    api(libs.jda)
}