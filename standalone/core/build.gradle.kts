plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.core"

dependencies {
    api(project(":fusion-files"))
}