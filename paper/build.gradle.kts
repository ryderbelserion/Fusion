plugins {
    `config-publish`
    `config-paper`
}

project.group = "${rootProject.name}.paper"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.nexomc.com/releases/")

    maven("https://repo.oraxen.com/releases/")

    maven("https://maven.devs.beer/")
}

dependencies {
    implementation(libs.configurate.gson) {
        exclude(
            group = "org.gson",
            module = "gson"
        )

        exclude(
            group = "org.spongepowered",
            module = "configurate-core"
        )

        exclude(
            group = "com.google.errorprone",
            module = "error_prone_annotations"
        )
    }

    compileOnly(libs.bundles.shared)

    api(libs.jalu) {
        exclude(
            group = "org.yaml",
            module = "snakeyaml",
        )
    }

    api(project(":fusion-core"))
}