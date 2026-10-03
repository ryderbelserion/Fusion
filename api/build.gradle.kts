plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.name}.api"

dependencies {
    compileOnly(project(":fusion-files"))
    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.text)
}