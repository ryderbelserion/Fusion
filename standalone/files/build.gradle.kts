plugins {
    `config-publish`

    `shadow-plugin`
}

project.group = "${rootProject.name}.files"

dependencies {
    api(libs.configurate.gson)
    api(libs.configurate.yaml)
    api(libs.jspecify)
    api(libs.jalu)
}