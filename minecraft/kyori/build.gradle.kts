plugins {
    `config-publish`

    `shadow-plugin`
}

project.group = "${rootProject.name}.kyori"

dependencies {
    compileOnly(libs.bundles.adventure)

    api(project(":fusion-core"))
}