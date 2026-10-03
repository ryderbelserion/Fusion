plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.group}.files"

dependencies {
    api(libs.configurate.gson)
    api(libs.configurate.yaml)
    api(libs.jspecify)
}