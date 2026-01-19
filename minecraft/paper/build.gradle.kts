plugins {
    `config-publish`
    `config-paper`
}

project.group = "${rootProject.name}.paper"

repositories {
    maven("https://repo.extendedclip.com/releases/")
}

dependencies {
    api(project(":fusion-kyori"))

    compileOnly(libs.placeholderapi)
}