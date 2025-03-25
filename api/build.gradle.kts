plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.api"
project.version = rootProject.version

dependencies {
    compileOnly(libs.configurate.yaml)

    compileOnly(libs.jetbrains)

    api(libs.configurate.jackson)

    api(libs.jalu)
}