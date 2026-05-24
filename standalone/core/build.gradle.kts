plugins {
    `config-publish`

    `shadow-plugin`
}

project.group = "${rootProject.name}.core"

dependencies {
    api(project(":fusion-files"))
}