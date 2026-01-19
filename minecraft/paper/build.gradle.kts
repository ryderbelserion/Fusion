plugins {
    `config-publish`
    `config-paper`
}

project.group = "${rootProject.name}.paper"

repositories {
    maven("https://repo.extendedclip.com/releases/")

    maven("https://repo.nexomc.com/releases/")

    maven("https://repo.oraxen.com/releases/")

    maven("https://maven.devs.beer/")
}

dependencies {
    api(project(":fusion-kyori"))

    compileOnly(libs.bundles.shared)
}