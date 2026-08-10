plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.name}.api"

dependencies {
    compileOnlyApi(libs.tinylog.impl)
    compileOnlyApi(libs.tinylog.api)
    compileOnlyApi(libs.jspecify)
}