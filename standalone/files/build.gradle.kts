plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.group}.files"

dependencies {
    api(project(":fusion-config"))

    api(libs.configurate.yaml)
    api(libs.jspecify)
}