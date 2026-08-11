plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.name}.api"

dependencies {
    compileOnly(libs.jspecify)
}