plugins {
    `config-publish`
    `config-paper`
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.nexomc.com/releases")

    maven("https://repo.oraxen.com/releases")

    maven("https://maven.devs.beer")
}

dependencies {
    api(project(":fusion-core")) {
        exclude("com.google.errorprone", "error_prone_annotations")
        exclude("org.spongepowered", "configurate-core")
    }

    compileOnly(libs.bundles.shared)
}