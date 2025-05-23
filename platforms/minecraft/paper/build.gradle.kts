plugins {
    `config-paper`
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.nexomc.com/releases")

    maven("https://repo.oraxen.com/releases")

    maven("https://maven.devs.beer")
}

dependencies {
    api(project(":fusion-adventure")) {
        exclude("org.yaml", "snakeyaml")
    }

    compileOnly(libs.bundles.shared)
}