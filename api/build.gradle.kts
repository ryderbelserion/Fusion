plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.name}.api"

dependencies {
    compileOnlyApi(libs.configurate.gson)
    compileOnlyApi(libs.configurate.yaml)
    compileOnlyApi(libs.jspecify)

    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.text)
}