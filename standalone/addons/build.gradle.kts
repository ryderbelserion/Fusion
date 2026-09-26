plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.group}.addons"
project.version = "1.0.0"

dependencies {
    api(libs.bundles.tinylog)
    api(libs.jspecify)
}