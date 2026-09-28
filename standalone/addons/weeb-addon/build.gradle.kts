plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.group}.weebs"
project.version = "1.0.0"

dependencies {
    compileOnlyApi(project(":fusion-addons"))
}