plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.files"

dependencies {
    api(libs.configurate.gson)
    api(libs.configurate.yaml)
    api(libs.jalu)
}