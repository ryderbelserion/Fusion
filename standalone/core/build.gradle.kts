plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.name}.core"

dependencies {
    api(project(":fusion-api"))

    api(libs.configurate.gson)
    api(libs.configurate.yaml)
    api(libs.jspecify)
}