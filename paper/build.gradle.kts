plugins {
    `config-paper`
}

project.group = "${rootProject.group}.paper"

dependencies {
    api(project(":core"))
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.nexomc.com/snapshots")

    maven("https://repo.oraxen.com/releases")

    maven("https://maven.devs.beer")
}

dependencies {
    compileOnly(libs.bundles.shared)
}