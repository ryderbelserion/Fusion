plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.kyori"

dependencies {
    compileOnly(libs.bundles.adventure)

    api(project(":fusion-core"))
}