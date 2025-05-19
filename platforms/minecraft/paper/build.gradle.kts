plugins {
    `config-paper`
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.nexomc.com/snapshots")

    maven("https://repo.oraxen.com/releases")

    maven("https://maven.devs.beer")
}

dependencies {
    api(project(":fusion-adventure"))

    compileOnly(libs.bundles.shared)
}