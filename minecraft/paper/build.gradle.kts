plugins {
    `config-publish`
    `paper-plugin`
}

project.group = "${rootProject.name}.paper"

repositories {
    maven("https://repo.extendedclip.com/releases")

    maven("https://repo.momirealms.net/releases")

    maven("https://repo.hibiscusmc.com/releases")

    maven("https://repo.nexomc.com/releases")

    maven("https://repo.oraxen.com/releases")

    maven("https://maven.devs.beer")
}

dependencies {
    api(project(":fusion-kyori")) {
        exclude(group = "org.jspecify")
    }

    compileOnly(libs.bundles.shared)
}