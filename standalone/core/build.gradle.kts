plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.group}.core"

dependencies {
    api(project(":fusion-files"))
    api(project(":fusion-api"))
}