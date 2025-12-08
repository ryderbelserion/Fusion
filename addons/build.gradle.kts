plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.addons"
project.version = "0.5.0"

dependencies {
    api(project(":fusion-files"))
    compileOnly(libs.log4j)
}