plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.group}.addons"
project.version = "1.6.0"

dependencies {
    api(libs.bundles.tinylog)
    api(libs.jspecify)
}