plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.name}.core"

dependencies {
    api(libs.configurate.gson)
    api(libs.configurate.yaml)
    api(libs.jspecify)

    api(project(":fusion-api"))
}