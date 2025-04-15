plugins {
    id("paper-plugin")

    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.nexomc.com/snapshots/")

    maven("https://repo.oraxen.com/releases/")

    maven("https://maven.devs.beer/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.bundles.shared) {
        exclude("org.bukkit", "*")
    }

    api(project(":fusion-core"))
}