plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.group}.addons"
project.version = "1.2.0"

dependencies {
    api(libs.jspecify)
}