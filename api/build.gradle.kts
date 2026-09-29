plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.name}.api"

dependencies {
    compileOnlyApi(project(":fusion-files"))

    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.text)
}