plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.group}.files"
project.version = "4.48.0"

dependencies {
    api(libs.configurate.gson)
    api(libs.configurate.yaml)
    api(libs.jspecify)
}