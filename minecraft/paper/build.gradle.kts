plugins {
    `config-publish`
    `paper-plugin`
}

project.group = "${rootProject.name}.paper"

dependencies {
    api(project(":fusion-kyori"))

    compileOnly(libs.bundles.shared)
}